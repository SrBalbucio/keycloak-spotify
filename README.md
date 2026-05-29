# Keycloak Spotify Identity Provider (SPI)

SPI de Identity Provider para autenticar usuários com Spotify no Keycloak via OAuth2.

## O que está implementado

- Identity Provider `spotify` para Keycloak broker OAuth2.
- Busca de perfil no endpoint `https://api.spotify.com/v1/me`.
- Mapeamento basico para `BrokeredIdentityContext`:
  - `id`, `username`, `email`, `display_name`, `country`, `image_url`
- Atributos para mappers nativos:
  - `spotify.id`
  - `spotify.display_name`
  - `spotify.country`
  - `spotify.image_url`
- Mapper custom opcional `spotify-attribute-mapper`.
- Registro de SPI com `AutoService`.
- DTOs com `Lombok`.

## Build

```bash
mvn clean package
```

## Deploy no Keycloak

1. Copie o JAR gerado em `target/` para o diretorio `providers/` do Keycloak.
2. Reinicie o Keycloak.
3. No Admin Console, crie um novo Identity Provider do tipo **Spotify**.

## Configuração recomendada

- Client ID / Client Secret do app Spotify.
- Scope configurável (padrao: `user-read-email user-read-private`).
- `Store Tokens`: habilitado para manter refresh token quando retornado pelo Spotify.
- `User Info URL`: configurável (padrão: `https://api.spotify.com/v1/me`).

## Mapper custom opcional

Use o mapper `Spotify Attribute Mapper` para copiar atributo do contexto broker para atributo de usuario no Keycloak.

Exemplo:

- Source attribute: `spotify.country`
- Target user attribute: `country`

## Testes

```bash
mvn test
```

