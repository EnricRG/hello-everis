# Decisiones de diseño

## Empaquetado por dominio

He decidido usar el empaquetado por dominio habitual de DDD. Debido a la simplicidad de algunos dominios, he omitido el 
uso del empaquetado habitual en arquitectura hexagonal. Aunque su tamaño podria no ser suficiente para justificar el uso
de esta estructura de paquetes, he decidido usar una estructura que marca las características de un diseño hexagonal en 
el paquete ```com.everis.hello.shoplist``` para mostrar un ejemplo. Este tendrá una forma similar a:
```
...
com.everis.hello.shoplist
├── app
│   ├── domain
│   │ 	├── model
│   │ 	├── exception
│   │ 	...
│   ├── ports
│   │ 	├── input
│   │ 	├── output
│   ...
├── infrastructure
│   ├── adapters
│   │ 	├── input
│   │ 	│   ├── rest
│   │ 	│   ...
│   │ 	├── output
│   │ 	│   ├── persistence
│   │ 	│   ...
│   │ 	...
│   ├── config
│   ...
...
```
Algunas clases se han simplificado, como por ejemplo puertos sobre una misma entidad con pocas operaciones se fusionan
en una sola interfaz. En aplicaciones grandes, sería adecuado separar las distintas operativas en interfaces diferentes.

## Listas únicas por usuario

Ante la ambigüedad en los requerimientos descritos en README.md sobre la unicidad de las listas, he decidido que para 
este ejemplo las listas sean únicas por usuario y nombre de lista.

## No usamos librerías de mapping entre objetos.

Existen librerías como MapStruct que permiten mapear objetos de unos a otros. Como los objetos de este dominio son 
extremadamente simples, se omite su uso. Recomiendo su uso en objetos de mayor tamaño, ya que reduce la cantidad de 
código "boilerplate" de manera similar a como lo hace _Lombok_.