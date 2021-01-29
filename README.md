# course project - cafeteria server
Fourth course year project. Server layer
____
## Endpoints:
### order
    GET /api/order/pages - количество страниц с заказами
    GET /api/order/{{page}} - передать {{page}} страницу
    GET /api/order/monitor ({"monitorCode" : "example"}) - передать заказ с заданным "monitor code"
    POST /api/order (order dto) - добавить заказ
    PATCH /api/order/id (Order status) - изменить статус заказа (статус передается в теле запроса)
    PATCH /api/order/{id}?status={{Order status}} - изменить статус заказа
    - GET /api/order?orderId={{id}} - передать заказ с заданным уникальным номером
### product
    GET /api/product/pages - количество страниц с продуктами
    GET /api/product?id={{id}} - передать продукцию с заданным уникальным номером
    GET /api/product/{{page}} - передать {{page}} страницу продукции
    GET /api/product{{page}}/building?name={{name}} - передать {{page}} страницу продукции из {{building name}} корпуса 
    POST /api/product (product dto) - добавить продукцию
    PATCH /api/product/{{id}}/available?available={{true or false}} - изменить наличие продукции
### product types
    GET /api/product/type/breakfast/{page}
    GET /api/product/type/starter/{page}
    GET /api/product/type/second/{page}
    GET /api/product/type/drinking/{page}
## DTO:
### order
    id : Long
    orderedOn : LocalDateTime
    monitorCode : String
    status : String
    productIds : ArrayList<Long> 
    buildingName : String
    userPersonalNumber : String
### product
    id : Long
    name : String
    price : Float
    available : Boolean
    description : String
    type : String
    image (byte array) : String
    nameBuilding : String
## Enum
### Order status
    CREATED - заказ создан
    PREPARING - заказ приготавливается
    READY - заказ готов
    CANCELLED - заказ отменен
    PAYMENT - ожидание оплаты