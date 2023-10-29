<!-- TOC -->
* [Introducción](#introducción)
* [Compilar y iniciar aplicación](#compilar-y-iniciar-aplicación)
* [Tests y cobertura de código](#tests-y-cobertura-de-código)
* [Decisiones de diseño e implementación](#decisiones-de-diseño-e-implementación)
  * [Listas únicas por usuario](#listas-únicas-por-usuario)
  * [Productos sin cantidad, solo la tipologia](#productos-sin-cantidad-solo-la-tipologia)
  * [Empaquetado por dominio](#empaquetado-por-dominio)
  * [No usamos librerías de mapping entre objetos.](#no-usamos-librerías-de-mapping-entre-objetos)
  * [Núcleo de negocio aislado con excepciones (hexagonal)](#núcleo-de-negocio-aislado-con-excepciones-hexagonal)
<!-- TOC -->

# Introducción

Este proyecto forma parte del proceso de selección de NTT Data Spain (conocida anteriormente como Everis). Consiste
en desarrollar un microservicio REST para gestion de listas de compra aplazada (listas "Para comprar más tarde").

Para más información se recomienda visitar el archivo [README.md original](./README_original.md).

# Compilar y iniciar aplicación

El archivo [README.md original](./README_original.md) contiene la información necesaria para desarrollar, compilar y 
ejecutar la aplicación. Complementándolo, estas son las propiedades que deben definirse para que la aplicación inicie:

| Propiedad               | Descripción                                                                                | Valor por defecto   |
|-------------------------|--------------------------------------------------------------------------------------------|---------------------|
| **everis.products.url** | URL base al servicio de información de productos. Formato _schema://host:port_             |                     |
| everis.products.timeout | Tiempo de espera en milisegundos de las peticiones al servicio de información de productos | 30000 (30 segundos) |

Las propiedades obligatorias se resaltan en **oscuro**.

Encontraréis ejemplos de archivos de configuración en los directorios de recursos [main/resources](./src/main/resources) 
y [test/resources](./src/test/resources).

# Tests y cobertura de código

Podrás encontrar un reporte de cobertura de código aquí: [Report](https://enricrg.github.io/hello-everis/coverage_reports/htmlReport/index.html)

Algunos métodos autogenerados de ciertos objetos como toString o constructor vacío no han quedado cubiertos por los 
tests, pero es necesario que estén. El objetivo ha sido cubrir casi el 100% del código de negocio, y el reporte lo 
valida.

# Decisiones de diseño e implementación

## Listas únicas por usuario

Ante la ambigüedad en los requerimientos descritos en README.md sobre la unicidad de las listas, he decidido que para
este ejemplo las listas sean únicas por usuario (lo llamaremos ```owner```) y nombre de lista.

## Productos sin cantidad, solo la tipologia

Parecería razonable que las listas dispusieran tanto de artículos como la cantidad de ese artículo para comprar más 
tarde. Sin embargo, como los requerimientos no lo exigen, para simplificar la implementación he decidido no disponer
de esas cantidades, y las listas solo contienen identificadores de artículo no repetidos. En términos conocidos, serían
más parecidas a una lista de favoritos o de deseos.

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
│   │ 	├── service
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