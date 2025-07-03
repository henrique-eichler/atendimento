#!/bin/bash
# setup.sh - Full Ollama VM setup for persistent model serving with GPU and auto-reload
# Usage: sudo ./setup.sh <model-name>

set -e

QDRANT_DIR=$(pwd)
QDRANT_CMP=$QDRANT_DIR/docker-compose.yml


# 1) Write docker compose of qdrant
echo "⚙️  docker compose of qdrant in $QDRANT_CMP"
cat > $QDRANT_CMP <<EOF
services:
  qdrant:
    image: qdrant/qdrant
    container_name: qdrant
    ports:
      - "6333:6333"
    volumes:
      - qdrant_data:/qdrant/storage
    restart: unless-stopped

volumes:
  qdrant_data:
EOF

# 2) Configure firewall
echo "🌐 Ensuring firewall allows port 6333..."
sudo ufw allow 6333/tcp || true

# 3) start docker service
docker compose up -d