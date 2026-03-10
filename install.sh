#!/bin/bash
# ─────────────────────────────────────────────────────────────
# Photoski — script de instalação (Linux)
# Executa como root: sudo bash install.sh
# ─────────────────────────────────────────────────────────────

set -e

# Detecta o usuário real (quem chamou sudo)
REAL_USER="${SUDO_USER:-$USER}"
REAL_HOME=$(eval echo "~$REAL_USER")

STORAGE_DIR="$REAL_HOME/photoski/storage"
BACKUP_DIR="$REAL_HOME/photoski/backup"
CONFIG_DIR="/etc/photoski"
CONFIG_FILE="$CONFIG_DIR/photoski.properties"

echo ""
echo "╔══════════════════════════════════════╗"
echo "║      Photoski — Instalação Linux     ║"
echo "╚══════════════════════════════════════╝"
echo ""

# ── 1. Cria pastas de storage e backup ──────────────────────
echo "▸ Criando pastas de storage e backup..."
mkdir -p "$STORAGE_DIR"
mkdir -p "$BACKUP_DIR"
chown -R "$REAL_USER:$REAL_USER" "$REAL_HOME/photoski"
echo "  Storage : $STORAGE_DIR"
echo "  Backup  : $BACKUP_DIR"

# ── 2. Cria arquivo de configuração ─────────────────────────
echo ""
echo "▸ Criando arquivo de configuração em $CONFIG_FILE..."
mkdir -p "$CONFIG_DIR"

cat > "$CONFIG_FILE" << EOF
# Photoski — configuração principal
# Editado automaticamente pelo install.sh

storage.path=$STORAGE_DIR
backup.path=$BACKUP_DIR
backup.hour=3
upload.max.bytes=524288000
EOF

echo "  Configuração criada."

# ── 3. Pergunta sobre pasta de backup personalizada ──────────
echo ""
read -p "▸ Deseja usar um HD externo ou pasta diferente para backup? [s/N] " USE_CUSTOM
if [[ "$USE_CUSTOM" =~ ^[Ss]$ ]]; then
    read -p "  Informe o caminho completo da pasta de backup: " CUSTOM_BACKUP
    if [ -d "$CUSTOM_BACKUP" ]; then
        sed -i "s|backup.path=.*|backup.path=$CUSTOM_BACKUP|" "$CONFIG_FILE"
        echo "  Backup configurado para: $CUSTOM_BACKUP"
    else
        echo "  Pasta não encontrada. Mantendo o padrão: $BACKUP_DIR"
    fi
fi

# ── 4. Systemd — iniciar Tomcat automaticamente ──────────────
echo ""
read -p "▸ Deseja configurar o Tomcat para iniciar automaticamente com o Linux? [s/N] " SETUP_SYSTEMD
if [[ "$SETUP_SYSTEMD" =~ ^[Ss]$ ]]; then
    read -p "  Informe o caminho do Tomcat (ex: /opt/tomcat): " TOMCAT_DIR

    if [ -f "$TOMCAT_DIR/bin/startup.sh" ]; then
        SERVICE_FILE="/etc/systemd/system/photoski.service"
        cat > "$SERVICE_FILE" << EOF
[Unit]
Description=Photoski — servidor de fotos (Tomcat)
After=network.target

[Service]
Type=forking
User=$REAL_USER
Environment=JAVA_HOME=$(dirname $(dirname $(readlink -f $(which java))))
ExecStart=$TOMCAT_DIR/bin/startup.sh
ExecStop=$TOMCAT_DIR/bin/shutdown.sh
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
EOF
        systemctl daemon-reload
        systemctl enable photoski
        echo "  Serviço criado e habilitado: photoski.service"
        echo "  Para iniciar agora: sudo systemctl start photoski"
    else
        echo "  startup.sh não encontrado em $TOMCAT_DIR/bin/. Pulando."
    fi
fi

# ── 5. Resumo final ──────────────────────────────────────────
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "  Instalação concluída!"
echo ""
echo "  Storage  : $(grep storage.path $CONFIG_FILE | cut -d= -f2)"
echo "  Backup   : $(grep backup.path  $CONFIG_FILE | cut -d= -f2)"
echo "  Config   : $CONFIG_FILE"
echo ""
echo "  Próximos passos:"
echo "  1. Faça o deploy do Photoski.war no Tomcat"
echo "  2. Acesse http://localhost:8080/Photoski"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
