#!/bin/bash

# Abrir uma nova aba e iniciar service-registry
gnome-terminal --tab --title="service-registry" -- bash -c "cd service-registry && mvn spring-boot:run; exec bash"

# Abrir uma nova aba e iniciar api-gateway
gnome-terminal --tab --title="api-gateway" -- bash -c "cd api-gateway && mvn spring-boot:run; exec bash"

# Abrir uma nova aba e iniciar config-server
gnome-terminal --tab --title="config-server" -- bash -c "cd config-server && mvn spring-boot:run; exec bash"
