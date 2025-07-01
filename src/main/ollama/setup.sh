#!/bin/bash
# setup.sh - Full Ollama VM setup for persistent model serving with GPU and auto-reload
# Usage: sudo ./setup.sh <model-name>

set -e

MODEL_NAME="${1:-deepseek-r1:7b}"
OLLAMA_HOME="/home/ollama"
OLLAMA_BIN="/usr/local/bin/ollama"
OLLAMA_DIR="$OLLAMA_HOME/.ollama/config"
OLLAMA_CFG="$OLLAMA_DIR/config.json"
OLLAMA_SVC="/etc/systemd/system/ollama.service"
RUN_SVC="/etc/systemd/system/ollama-run-deepseek.service"
PING_SVC="/etc/systemd/system/ollama-ping-deepseek.service"

# 1) GPU check
echo "🔍 Checking for NVIDIA GPU..."
if command -v nvidia-smi &>/dev/null && nvidia-smi &>/dev/null; then
  echo "✅ NVIDIA GPU detected."
else
  echo "⚠️  No NVIDIA GPU detected or driver issue."
fi

# 2) Install Ollama if missing
echo "⬇️  Installing Ollama if missing..."
if ! command -v ollama &>/dev/null; then
  curl -fsSL https://ollama.com/install.sh | sh
else
  echo "✅ Ollama already installed."
fi

# 3) Tear down old services
echo "🛑 Stopping and disabling existing services..."
systemctl stop ollama.service ollama-run-deepseek.service ollama-ping-deepseek.service 2>/dev/null || true
systemctl disable ollama.service ollama-run-deepseek.service ollama-ping-deepseek.service 2>/dev/null || true
journalctl --vacuum-time=1s 2>/dev/null || true
rm -rf $OLLAMA_HOME $OLLAMA_SVC $RUN_SVC $PING_SVC

# 4) Write ollama configuration
echo "⚙️  Writing ollama config to $OLLAMA_CFG"
mkdir -p $OLLAMA_DIR
cat > $OLLAMA_CFG <<EOF
{
  "host": "0.0.0.0"
}
EOF
chown -R ollama:ollama $OLLAMA_HOME

# 5) Write ollama service unit
echo "⚙️  Writing ollama service to $OLLAMA_SVC"
cat > $OLLAMA_SVC <<EOF
[Unit]
Description=Ollama Service
After=network-online.target

[Service]
Type=simple
User=ollama
Group=ollama
Environment="HOME=/home/ollama"
Environment=OLLAMA_HOST=0.0.0.0
Environment=OLLAMA_ACCELERATOR=gpu
Environment="PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games:/snap/bin"
ExecStart=/usr/local/bin/ollama serve
Restart=always
RestartSec=3

[Install]
WantedBy=default.target
EOF

# 6) Write run service unit
echo "⚙️  Writing run service to $RUN_SVC"
cat > "$RUN_SVC" <<EOF
[Unit]
Description=Load Ollama model $MODEL_NAME
After=ollama.service
Wants=ollama.service

[Service]
Type=oneshot
RemainAfterExit=yes
User=ollama
Group=ollama
Environment=PATH=/usr/local/bin:/usr/bin:/bin
Environment=OLLAMA_HOST=0.0.0.0
Environment=OLLAMA_ACCELERATOR=gpu
ExecStart=/usr/local/bin/ollama run $MODEL_NAME

[Install]
WantedBy=multi-user.target
EOF

# 7) Write ping service unit
echo "⚙️  Writing ping service to $PING_SVC"
cat > "$PING_SVC" <<EOF
[Unit]
Description=Ping Ollama model $MODEL_NAME every 4m
After=ollama-run-deepseek.service
Wants=ollama-run-deepseek.service

[Service]
Type=simple
User=ollama
Group=ollama
Environment=PATH=/usr/local/bin:/usr/bin:/bin
ExecStart=/usr/bin/env bash -c 'while true; do curl -fs -X POST http://localhost:11434/api/generate -H "Content-Type: application/json" -d "{\"model\":\"$MODEL_NAME\",\"prompt\":\"ping\",\"stream\":false}"; sleep 1m; done'
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
EOF

# 8) Configure firewall
echo "🌐 Ensuring firewall allows port 11434..."
ufw allow 11434/tcp || true

# 9) Start core Ollama server
echo "🚀 Starting core Ollama server..."
systemctl start ollama.service 2>/dev/null || true
systemctl enable ollama.service 2>/dev/null || true

# 10) Wait for Ollama readiness
echo -n "⏳ Waiting for Ollama to respond"
for i in {1..60}; do
  if curl -s http://localhost:11434/version &>/dev/null; then
    echo " - ready!"
    break
  fi
  echo -n "."
  sleep 1
  if (( i == 60 )); then
    echo -e "\n❌ Ollama did not respond in time."
    exit 1
  fi
done

# 11) Pull model if missing
echo "📥 Ensuring model '$MODEL_NAME' is available..."
if ! "$OLLAMA_BIN" list | grep -q "$MODEL_NAME"; then
  "$OLLAMA_BIN" pull "$MODEL_NAME"
  echo "✅ Model '$MODEL_NAME' pulled."
else
  echo "✅ Model '$MODEL_NAME' already present."
fi

# 12) Enable & start services
echo "🔄 Enabling services..."
systemctl daemon-reload
systemctl enable ollama.service ollama-run-deepseek.service ollama-ping-deepseek.service

# 13) Final status
echo "✅ Setup complete. Ollama model '$MODEL_NAME' is configured and will persist across reboots."

# 14) Restarts machine
echo "⚠️ Reboots machine."
shutdown -r