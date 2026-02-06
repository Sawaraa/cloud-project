# Етап 1: Збірка React додатка
FROM node:20-alpine AS build
WORKDIR /app

COPY package.json ./

# Встановлюємо залежності та примусово додаємо ajv, якого не вистачає
RUN npm install --legacy-peer-deps && \
    npm install ajv --legacy-peer-deps && \
    npm cache clean --force

COPY . .

# Виправляємо синтаксис ENV (додаємо "=")
ENV NODE_OPTIONS="--max-old-space-size=4096"
RUN npm run build

# Етап 2: Роздача через Nginx
FROM nginx:stable-alpine
COPY --from=build /app/build /usr/share/nginx/html
RUN echo "server { listen 80; location / { root /usr/share/nginx/html; index index.html; try_files \$uri \$uri/ /index.html; } }" > /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]