# Decisiones de diseño

## Empaquetado por dominio, sin empaquetado por capas

He decidido no usar el empaquetado por capas debido a la simplicidad de los dominios. En una aplicación real, separaria
las diferentes capas dentro del propio dominio. Un ejemplo de paquete de dominio complejo:
```
...
dominiocomplejo
├── app
│   ├── domain
│   │ 	├── model
│   │ 	├── exception
│   │ 	...
│   ├── ports
│   ├── usecase
│   ...
├── infrastructure
│   ├── adapters
│   │ 	├── rest
│   │ 	├── persistence
│   │ 	...
│   ├── config
│   ...
...
```