Primero hemos creado Producto que es el objeto sobre el que gira entorno la aplicación este contiene sus respectivo atributos para poder ser añadido en una lista de productos o un ticket, solo cuenta con una clase para inicializar objetos, getters y setters.

Este contiene una categoría de tipo enum con todos los tipos de categoría que tiene un producto para poder aplicar los descuentos.

Luego hemos creado la clase ProductCatalog que contiene un hashmap donde se guardan todos los productos distintos que puede tener el sistema, cuenta con su método de inicialización y sus otros métodos para poder hacer operaciones de añadir, borrado y consulta. Como la tienda cuenta con ticket y un catalogo vimos necesario crear un objeto donde poder guardar los objetos del sistema y que sobre ese objeto de hagan los cambios pertinentes que necesitemos, representado al "sistema de productos".

Para Ticket hemos necesitado crear un objeto ticketItem ya que era la mejor manera que vimos para crear un objeto producto con la cantidad que queramos añadir de el al hashmap de ticket porque en ticket tenemos los objetos con ids únicos como clave y en cada clave hay un producto con su cantidad en ese momento así las consultas de modificar cantidad de un articulo se hacen más rápidas buscando por id en hashmap.

Estas dos clases ProductCatalog y Ticket son las que representaran en tienda las dos maneras de guardar productos en ticket o en sistema. 
Tienda es la clase de ejecución principal, al final cuando queremos hacer algo es una operación a realizar en Tienda indistintamente de si es ticket o añadir un producto al catálogo, se pedirán instrucciones hasta que se decida terminar la sesión con exit.

Por ultimo la clase commandNames que es simplemente para no llenar tienda de texto, cuenta con los nombres de los comandos pertenecientes al help, el único motivo era por estética para no llenar tienda de texto no necesario.
