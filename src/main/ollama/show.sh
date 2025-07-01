#!/bin/bash

clear

echo /home/ollama/.ollama/config/config.json
echo ---------------------------------------
cat /home/ollama/.ollama/config/config.json
echo ""

echo /etc/systemd/system/ollama.service
echo ----------------------------------
cat /etc/systemd/system/ollama.service
echo ""

echo /etc/systemd/system/ollama-run-deepseek.service
echo -----------------------------------------------
cat /etc/systemd/system/ollama-run-deepseek.service
echo ""

echo /etc/systemd/system/ollama-ping-deepseek.service
echo ------------------------------------------------
cat /etc/systemd/system/ollama-ping-deepseek.service
echo ""

echo ollama service status
echo ---------------------
systemctl --no-page status ollama
echo ""

journalctl --no-page -u ollama.service -n 20
echo ""

echo ollama-run-deepseek service status
echo ----------------------------------
systemctl --no-page status ollama-run-deepseek
echo ""

journalctl --no-page -u ollama.service -n 20
echo ""

echo ollama-ping-deepseek service status
echo -----------------------------------
systemctl --no-page status ollama-ping-deepseek
echo ""

journalctl --no-page -u ollama.service -n 20
echo ""

echo ollama list
echo -----------
ollama list
echo ""

echo ollama ps
echo ---------
ollama ps
echo ""

echo "ss -ltnp | grep 11434"
echo ---------------------
ss -ltnp | grep 11434
