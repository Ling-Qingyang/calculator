# ==============================================================================
# Dockerfile for Calculator Web Frontend Service
# Service: web
# Base Image: Nginx 1.27 Alpine (Minimal Footprint & High Performance)
# Exposed Port: 80
# ==============================================================================

# FROM: Lightweight Alpine-based Nginx official image (~23MB)
FROM nginx:alpine

# LABEL: Provide image authorship and service identification metadata
LABEL maintainer="Ling Qingyang <279034363+Ling-Qingyang@users.noreply.github.com>"
LABEL service="calculator-web"
LABEL course="Software Development Tools - Lab 7"

# WORKDIR: Set default Nginx HTML root directory
WORKDIR /usr/share/nginx/html

# RUN: Clean up default Nginx sample index files to guarantee clean deployment
RUN rm -rf ./*

# COPY: Install custom Nginx reverse proxy routing configuration
# Note: Inter-container calls route through Docker Compose internal DNS hostname 'backend:8000'
COPY nginx.conf /etc/nginx/conf.d/default.conf

# COPY: Copy static website assets (HTML structure, CSS styles, JavaScript logic)
COPY index.html style.css app.js ./

# EXPOSE: Declare that container serves incoming HTTP web traffic on standard port 80
EXPOSE 80

# STOPSIGNAL: Graceful shutdown signal for Nginx worker processes
STOPSIGNAL SIGQUIT

# CMD: Execute Nginx in the foreground to prevent container from immediately exiting
CMD ["nginx", "-g", "daemon off;"]
