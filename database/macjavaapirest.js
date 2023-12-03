// Creamos el usuario administrador de la base de datos
// con sus daatos de conexion y los roles que tendra
// creo q es para mongoexpress

db.createUser({
    user: 'admin',
    pwd: 'adminPassword123',
    roles: [
        {
            role: 'readWrite',
            db: 'macjavaapirest',
        },
    ],
});

// Nos conectamos a la base de datos world
db = db.getSiblingDB('macjavaapirest');

// Creamos la coleccion city
db.createCollection('order');

// Insertamos los datos de la coleccion pedidos
db.order.insertMany([
    {
        clientUUID: 'f47ac10b-58cc-4372-a567-0e02b2c3d479',
        workerUUID: '550e8400-e29b-41d4-a716-446655440000',
        restaurantId: 1,
        orderedProducts: [
            {
                quantity: 2,
                productId: 1,
                productPrice: 10.50,
                totalPrice: 21.00,
            },
            {
                quantity: 3,
                productId: 3,
                productPrice: 20.00,
                totalPrice: 60.00,
            },
        ],
        totalPrice: 81.00,
        totalQuantityProducts: 5,
        isPaid: false,
        createdAt: '2023-10-23T12:57:17.3411925',
        updatedAt: '2023-10-23T12:57:17.3411925',
        isDeleted: false,
    },
    {
        clientUUID: "168ecf01-a6b6-4a56-ba5a-cb26199f21ab", //esta mal, no existe ese uuid, es para probar put
        workerUUID: "550e8400-e29b-41d4-a716-446655440000",
        restaurantId: 2,
        orderedProducts: [
            {
                quantity: 4,
                productId: 1,
                productPrice: 10.5,
                totalPrice: 42.0
            }
        ],
        totalPrice: 42.0,
        totalQuantityProducts: 4,
        isPaid: false,
        createdAt: '2022-10-23T12:57:17.3411925',
        updatedAt: '2022-10-23T12:57:17.3411925',
        isDeleted: false,
        id: "6569a77e44ce487c8247abbb"
    }
]);
