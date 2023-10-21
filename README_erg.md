# Decisiones de diseño

## Empaquetado por dominio

He decidido usar el empaquetado por dominio habitual de DDD. Debido a la simplicidad de algunos dominios, he omitido el 
uso del empaquetado típico en arquitectura hexagonal. Aunque su tamaño podria no ser suficiente para justificar el uso
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
este ejemplo las listas sean únicas por usuario (lo llamaremos ```owner```) y nombre de lista.

## No usamos librerías de mapping entre objetos.

Existen librerías como MapStruct que permiten mapear objetos de unos a otros. Como los objetos de este dominio son 
extremadamente simples, se omite su uso. Recomiendo su uso en objetos de mayor tamaño, ya que reduce la cantidad de 
código "boilerplate" de manera similar a como lo hace _Lombok_.

## Núcleo de negocio aislado con excepciones (hexagonal)

La arquitectura hexagonal tiene como uno de sus principales objetivos separar el núcleo de negocio (el hexágono central)
de los detalles de infraestructura de sus adaptadores. Aunque en el ideal solo tendríamos código de negocio própio
o del lenguaje en este núcleo, trabajamos con Java y JavaEE/JakartaEE que nos proporciona unas librerías de negocio
altamente funcionales y testeadas. 

En esta aplicación, permitiremos anotaciones de JakartaEE en el núcleo de negocio (como ```@Transactional```). Esto nos 
permite definir funcionalmente que nuestra operativa se debe ejecutar de forma transaccional y transmitir de forma
transparente la gestión de la transacción a la capa de persistencia.

Otra excepción es la anotación ```@ResponseStatus``` de Spring. La usaremos en las excepciones para simplificar el mapeo de 
HTTP Status codes en función de la excepción. En mi puesto actual elaboré un diseño en el que todas nuestras excepciones
de negocio parten de una excepción própia AppException que tiene un campo de tipo ```enum``` que describe funcionalmente
el tipo de error, sin detalles de implementación (```input_validation_err, business_err, not_found_err, communication_err```).
Luego, cada aplicación tiene una anotación que incluye un @ControllerAdvice que lee las excepciones que le llegan, y 
si se trata de una AppException mapea el campo al status code pertinente. Actualmente, este diseño se encuentra en 
producción en la mayoría de nuestros microservicios. He omitido un diseño similar en esta aplicación para simplificar 
la implementación, pero considero que sería una mejor solución al uso de la anotación ```@ResponseStatus```.