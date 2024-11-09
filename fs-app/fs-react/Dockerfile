FROM node:21.5-bullseye AS build

WORKDIR /usr/src/app

COPY package*.json ./

RUN --mount=type=cache,target=/usr/src/app/.npm \
    npm set cache /usr/src/app/.npm && \
    npm install

COPY . .

RUN npm run build

FROM nginx:1.27.2-alpine

EXPOSE 80

COPY --from=build /usr/src/app/build /usr/share/nginx/html
