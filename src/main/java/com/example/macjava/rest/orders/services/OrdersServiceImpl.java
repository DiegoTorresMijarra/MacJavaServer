package com.example.macjava.rest.orders.services;

import com.example.macjava.rest.clients.repository.ClientsRepository;
import com.example.macjava.rest.orders.dto.OrderSaveDto;
import com.example.macjava.rest.orders.dto.OrderType;
import com.example.macjava.rest.orders.dto.OrderUpdateDto;
import com.example.macjava.rest.orders.exceptions.OrderBadRequest;
import com.example.macjava.rest.orders.exceptions.OrderNotFound;
import com.example.macjava.rest.orders.exceptions.ProductOrderedNotFound;
import com.example.macjava.rest.orders.mappers.OrderMapper;
import com.example.macjava.rest.orders.models.Order;
import com.example.macjava.rest.orders.models.OrderedProduct;
import com.example.macjava.rest.orders.repositories.OrdersCrudRepository;
import com.example.macjava.rest.products.repository.ProductRepository;
import com.example.macjava.rest.restaurants.repositories.RestaurantRepository;
import com.example.macjava.rest.workers.repositories.WorkersCrudRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implementación del servicio para los pedidos
 */
@Service
@Slf4j
@CacheConfig(cacheNames = {"orders"})
public class OrdersServiceImpl implements OrdersService{
    private final OrdersCrudRepository ordersCrudRepository;
    private final ClientsRepository clientsRepository;
    private final ProductRepository productRepository;
    private final RestaurantRepository restaurantRepository;
    private final WorkersCrudRepository workersCrudRepository;

    @Autowired
    public OrdersServiceImpl(OrdersCrudRepository ordersCrudRepository, ClientsRepository clientsRepository, ProductRepository productRepository, RestaurantRepository restaurantRepository, WorkersCrudRepository workersCrudRepository) {
        this.ordersCrudRepository = ordersCrudRepository;
        this.clientsRepository = clientsRepository;
        this.productRepository = productRepository;
        this.restaurantRepository = restaurantRepository;
        this.workersCrudRepository = workersCrudRepository;
    }
    /**
     * Busca todos los pedidos
     * @return todos los pedidos
     */
    @Override
    public List<Order> findAll() {
        log.info("Listando todos los pedidos");
        return ordersCrudRepository.findAll();
    }

    /**
     * Busca todos los pedidos pageados
     * @param pageable
     * @return todos los pedidos pageados
     */
    @Override
    public Page<Order> findAll(Pageable pageable) {
        log.info("Listando todos los pedidos pageados");
        return ordersCrudRepository.findAll(pageable);
    }

    /**
     * Busca un pedido por su id
     * @param objectId id del pedido
     * @return pedido
     * @throws OrderNotFound si no existe el pedido
     */
    @Override
    @Cacheable(key = "#objectId")
    public Order findById(ObjectId objectId) {
        log.info("Buscando un pedido por su id");
        return ordersCrudRepository.findById(objectId).orElseThrow(()->new OrderNotFound(objectId.toHexString()));
    }

    /**
     * Guarda un pedido
     * @param order pedido a guardar
     * @return pedido guardado
     */
    @Override
    @Transactional //no se si es necesario repetirlo en controller tb
    @CachePut(key = "#result.id")
    public Order save(OrderSaveDto order) {
        log.info("Guardando order : {}", order);
        checkOrderIds(order);
        checkOrderedProducts(order);
        return ordersCrudRepository.save(OrderMapper.saveToModel(order));
    }

    /**
     * Elimina un pedido por su id
     * Llama al metodo findById para encontrar y comprobar que existe el pedido
     * @param objectId id del pedido
     */
    @Override
    @CacheEvict(key = "#objectId")
    public void deleteById(ObjectId objectId) {
        log.info("Eliminando un pedido por su id: "+objectId.toHexString());
        findById(objectId);
        ordersCrudRepository.deleteById(objectId); //no hacemos el return, porque la comida no se devuelve hehe
    }

    /**
     * Como puede recibir en orderedProducts tanto un null, como una lista vacia ({}) o invalida (solo checkea que sea valida).
     * Vamos a trabajar con que si le pasamos un null, no hay que validar la lista, porque el mapper guardara el valor original. <br>
     * En caso contrario, la valida antes de actualizar y el actualizar, no dejara que pase una lista vacia
     *
     * @param objectId id del pedido
     * @param dto pedido a actualizar
     * @return pedido actualizado
     */
    @Override
    @CachePut(key = "#result.id")
    public Order updateOrder(ObjectId objectId, OrderUpdateDto dto) {
        log.info("Actualizando un pedido por su id: "+objectId.toHexString());
        if(dto.getOrderedProducts()!=null) {
            checkOrderedProducts(dto);
        }
        checkOrderIds(dto);
        var original = findById(objectId);
        var updated = OrderMapper.updateToModel(original,dto);
        updated.setId(objectId);
        return ordersCrudRepository.save(updated);
    }

    /**
     * Busca los pedidos de un cliente
     * Para encontrar al cliente, llama al metodo findById del repositorio de clientes
     * @param clientUUID uuid del cliente
     * @param pageable paginacion
     * @return pedidos del cliente
     */
    @Override
    public Page<Order> findByClientUUID(UUID clientUUID, Pageable pageable) {
        log.info("Buscando los pedidos del cliente con uuid: " + clientUUID);
        return ordersCrudRepository.findByClientUUID(clientUUID, pageable);
    }

    /**
     * Comprueba si existe algun pedido del cliente
     * @param clientUUID uuid del cliente
     * @return true si existe algun pedido del cliente, false en caso contrario
     */
    @Override
    public Boolean existsByClientUUID(UUID clientUUID) {
        log.info("Comprobando si existe algun pedido del cliente con uuid: " + clientUUID);
        return ordersCrudRepository.existsByClientUUID(clientUUID);
    }

    /**
     * Busca los pedidos de un trabajador y los pagina
     * @param workerUUID  uuid del trabajador
     * @param pageable paginacion
     * @return pedidos del trabajador
     */
    @Override
    public Page<Order> findByWorkerUUID(UUID workerUUID, Pageable pageable) {
        log.info("Buscando los pedidos del trabajador con uuid: " + workerUUID);
        return ordersCrudRepository.findByWorkerUUID(workerUUID, pageable);
    }

    /**
     * Comprueba si existe algun pedido del trabajador
     * @param workerUUID uuid del trabajador
     * @return true si existe algun pedido del trabajador, false en caso contrario
     */
    @Override
    public Boolean existsByWorkerUUID(UUID workerUUID) {
        log.info("Comprobando si existe algun pedido del cliente con uuid: " + workerUUID);
        return ordersCrudRepository.existsByWorkerUUID(workerUUID);
    }

    /**
     * Busca los pedidos de un restaurante y los pagina
     * @param restaurantId id del restaurante
     * @param pageable paginacion
     * @return pedidos del restaurante
     */
    @Override
    public Page<Order> findByRestaurantId(Long restaurantId, Pageable pageable) {
        log.info("Buscando los pedidos del restaurante con id: "+restaurantId);
        return ordersCrudRepository.findByRestaurantId(restaurantId, pageable);
    }

    /**
     * Comprueba si existe algun pedido del restaurante
     * @param restaurantId id del restaurante
     * @return true si existe algun pedido del restaurante, false en caso contrario
     */
    @Override
    public Boolean existsByRestaurantId(Long restaurantId) {
        log.info("Comprobando si existe algun pedido en el restaurante con id: "+restaurantId);
        return ordersCrudRepository.existsByRestaurantId(restaurantId);
    }

    /**
     * Actualiza si el pedido ha sido pagado
     * @param objectId id del pedido
     * @param isPaid indica si el pedido ha sido pagado
     * @return pedido actualizado
     */
    @Override
    public Order updateIsPaidById(ObjectId objectId, Boolean isPaid) {
        log.info("Actualizando isPaid del pedido con id: "+objectId.toHexString()+" a "+ isPaid);
        var original=findById(objectId);
        original.setIsPaid(isPaid);
        return ordersCrudRepository.save(original);
    }

    /**
     * Actualiza si el pedido ha sido eliminado
     * @param objectId id del pedido
     * @param isDeleted indica si el pedido quiere ser eliminado
     * @return pedido actualizado
     */
    @Override
    public Order updateIsDeletedById(ObjectId objectId, Boolean isDeleted) {
        log.info("Actualizando isDeleted del pedido con id: "+objectId.toHexString()+" a "+ isDeleted);
        var original=findById(objectId);
        original.setIsDeleted(isDeleted);
        return ordersCrudRepository.save(original);
    }

    /**
     * No hay que verificar que sea nula, porque esta es validada antes de ser checkeada. <br>
     * El save desde las validaciones de las constraints y update en el metodo <br>
     * Tras verificar actualiza la cantidad del producto, puede generar bad smell, corregir -> principio de unidad
     * @param order
     * @param <T>
     */
    public <T extends OrderType> void checkOrderedProducts(T order) {
        log.info("Validando la lista de productos del pedido {}",order);
        var list= order.getOrderedProducts();
        if(list.isEmpty()) {
            throw new OrderBadRequest("La lista de productos no puede ser vacia");
        }
        for(OrderedProduct orderedProduct : list){
            var repositoryProduct = productRepository.findById(orderedProduct.getProductId()).orElseThrow(()->new ProductOrderedNotFound(orderedProduct.getProductId()));
            if(orderedProduct.getProductPrice()!=repositoryProduct.getPrecio()) {
                throw new OrderBadRequest("El precio del producto con id"+orderedProduct.getProductId()+" no coincide con el de la base de datos");
            }
            if(orderedProduct.getQuantity()>repositoryProduct.getStock()) {
                throw new OrderBadRequest("La cantidad del producto con id"+orderedProduct.getProductId()+" no coincide con el de la base de datos");
            }
            //Actualizamos la cantidad del producto en el repo
            repositoryProduct.setStock(repositoryProduct.getStock()-orderedProduct.getQuantity());
            productRepository.save(repositoryProduct);
            //Actualizamos el total del pedido
            orderedProduct.calculateTotalPrice();
        }
    }

    /**
     * Verifica que los ids del pedido sean validos
     * @param order pedido
     * @param <T>
     */
    public <T extends OrderType> void checkOrderIds(T order) {
        log.info("Validando las referencias del pedido {}",order);
        UUID clientUUID = order.getClientUUID();
        if (clientUUID!=null){
            var client = clientsRepository.findById(clientUUID)
                    .orElseThrow(()->new OrderBadRequest("El cliente con UUID "+clientUUID+" no existe"));
        }
        UUID workerUUID = order.getWorkerUUID();
        if (workerUUID!=null){
            var worker = workersCrudRepository.findById(workerUUID)
                    .orElseThrow(()->new OrderBadRequest("El trabajador con UUID "+workerUUID+" no existe"));
        }
        Long restaurantId = order.getRestaurantId();
        if (restaurantId!=null){
            var restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(()->new OrderBadRequest("El restaurante con id "+restaurantId+" no existe"));
        }
    }
}
