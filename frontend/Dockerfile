FROM node:18-slim

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

EXPOSE 3000
CMD ["npm", "run", "dev"]


# FROM node:slim AS builder
# WORKDIR /app

# COPY ./ ./
# RUN npm install
# RUN npm run build
# FROM nginx:stable-alpine
# COPY --from=builder /app/dist /usr/share/nginx/html
# EXPOSE 80 
# CMD ["nginx", "-g", "daemon off;"]
