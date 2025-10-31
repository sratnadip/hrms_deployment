#!/bin/bash

# HRMS Monitoring and Maintenance Script

show_status() {
    echo "=== HRMS Service Status ==="
    docker compose -f docker-compose-prod.yml ps
    echo ""
    
    echo "=== Resource Usage ==="
    docker stats --no-stream --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.NetIO}}"
    echo ""
    
    echo "=== Service Health ==="
    echo -n "Backend Health: "
    curl -s http://localhost:8080/actuator/health | jq -r '.status' 2>/dev/null || echo "Not responding"
    
    echo -n "Frontend Status: "
    curl -s -o /dev/null -w "%{http_code}" http://localhost:4200 2>/dev/null || echo "Not responding"
    
    echo -n "MySQL Status: "
    docker exec hrms-mysql mysqladmin ping -h localhost -u root -proot 2>/dev/null && echo "OK" || echo "Not responding"
    echo ""
}

show_logs() {
    echo "=== Recent Logs ==="
    echo "Choose service to view logs:"
    echo "1. All services"
    echo "2. Backend only"
    echo "3. Frontend only"
    echo "4. MySQL only"
    read -p "Enter choice (1-4): " choice
    
    case $choice in
        1) docker compose -f docker-compose-prod.yml logs --tail=50 ;;
        2) docker compose -f docker-compose-prod.yml logs backend --tail=50 ;;
        3) docker compose -f docker-compose-prod.yml logs frontend --tail=50 ;;
        4) docker compose -f docker-compose-prod.yml logs mysql-service --tail=50 ;;
        *) echo "Invalid choice" ;;
    esac
}

restart_services() {
    echo "=== Restarting Services ==="
    echo "Choose service to restart:"
    echo "1. All services"
    echo "2. Backend only"
    echo "3. Frontend only"
    echo "4. MySQL only"
    read -p "Enter choice (1-4): " choice
    
    case $choice in
        1) docker compose -f docker-compose-prod.yml restart ;;
        2) docker compose -f docker-compose-prod.yml restart backend ;;
        3) docker compose -f docker-compose-prod.yml restart frontend ;;
        4) docker compose -f docker-compose-prod.yml restart mysql-service ;;
        *) echo "Invalid choice" ;;
    esac
}

backup_database() {
    echo "=== Database Backup ==="
    BACKUP_DIR="backups"
    mkdir -p $BACKUP_DIR
    BACKUP_FILE="$BACKUP_DIR/hrms_backup_$(date +%Y%m%d_%H%M%S).sql"
    
    docker exec hrms-mysql mysqldump -u root -proot hrmsportal > $BACKUP_FILE
    echo "Database backed up to: $BACKUP_FILE"
}

cleanup_system() {
    echo "=== System Cleanup ==="
    echo "This will remove unused Docker images and containers"
    read -p "Are you sure? (y/N): " confirm
    
    if [[ $confirm == [yY] ]]; then
        docker system prune -f
        docker volume prune -f
        echo "Cleanup completed"
    fi
}

case "$1" in
    status|s)
        show_status
        ;;
    logs|l)
        show_logs
        ;;
    restart|r)
        restart_services
        ;;
    backup|b)
        backup_database
        ;;
    cleanup|c)
        cleanup_system
        ;;
    *)
        echo "HRMS Monitoring Script"
        echo "Usage: $0 {status|logs|restart|backup|cleanup}"
        echo ""
        echo "Commands:"
        echo "  status   - Show service status and health"
        echo "  logs     - View service logs"
        echo "  restart  - Restart services"
        echo "  backup   - Backup database"
        echo "  cleanup  - Clean up Docker system"
        echo ""
        echo "Short forms: s, l, r, b, c"
        ;;
esac
