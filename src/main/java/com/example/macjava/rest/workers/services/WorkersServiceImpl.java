package com.example.macjava.rest.workers.services;

import com.example.macjava.rest.categories.models.Position;
import com.example.macjava.rest.categories.services.PositionServiceImpl;
import com.example.macjava.rest.workers.dto.WorkersSaveDto;
import com.example.macjava.rest.workers.dto.WorkersUpdateDto;
import com.example.macjava.rest.workers.exceptions.WorkersNotFound;
import com.example.macjava.rest.workers.mappers.WorkersMapper;
import com.example.macjava.rest.workers.models.Workers;
import com.example.macjava.rest.workers.repositories.WorkersCrudRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio para la entidad Workers
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "workers")
public class WorkersServiceImpl implements WorkersService{
    private final WorkersCrudRepository workersCrudRepository;
    private final PositionServiceImpl positionService;
    @Autowired
    public WorkersServiceImpl(WorkersCrudRepository workersCrudRepository, PositionServiceImpl positionService) {
        log.info("Iniciando Servicio de Empleados");
        this.workersCrudRepository = workersCrudRepository;
        this.positionService = positionService;
    }

    /**
     * Busca todos los trabajadores con los criterios de búsqueda
     * @param name opcional: nombre del trabajador
     * @param surname opcional: apellido del trabajador
     * @param age opcional: edad del trabajador
     * @param phone opcional: telefono del trabajador
     * @param isDeleted opcional: indica si el trabajador esta borrado o no
     * @param antiquierityMin opcional: antiguedad minima del trabajador
     * @param antiquierityMax opcional: antiguedad maxima del trabajador
     * @param positionId opcional: id de la posición del trabajador
     * @param pageable paginación
     * @return lista de trabajadores encontrados
     */
    @Override
    public Page<Workers> findAll(Optional<String> name, Optional<String> surname, Optional<Integer> age, Optional<String> phone,
                                 Optional<Boolean> isDeleted, Optional<Integer> antiquierityMin, Optional<Integer> antiquierityMax,
                                 Optional<Integer> positionId, Pageable pageable) {

        log.info("Buscando Empleados");
        //Criterio por nombre
        Specification<Workers> specName=(root, query, criteriaBuilder)->
                name.map(d->criteriaBuilder.equal(root.get("name"),d))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        //Criterio por apellido
        Specification<Workers> specSurname=(root, query, criteriaBuilder)->
                surname.map(d->criteriaBuilder.equal(root.get("surname"),d))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        //Criterio por edad
        Specification<Workers> specAge=(root, query, criteriaBuilder)->
                age.map(d->criteriaBuilder.equal(root.get("age"),d))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        //Criterio por telefono
        Specification<Workers> specPhone=(root, query, criteriaBuilder)->
                phone.map(d->criteriaBuilder.equal(root.get("phone"),d))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        //Criterio por isDeleted
        Specification<Workers> specIsDeleted=(root, query, criteriaBuilder)->
                isDeleted.map(d->criteriaBuilder.equal(root.get("isDeleted"),d))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        //Criterio por antiguedad minima
        Specification<Workers> specAntiquierityMin=(root, query, criteriaBuilder)->
                antiquierityMin.map(d-> LocalDateTime.now().minusYears(d))
                        .map(e-> criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"),e))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        //Criterio por antiguedad maxima
        Specification<Workers> specAntiquierityMax=(root, query, criteriaBuilder)->
                antiquierityMax.map(d-> LocalDateTime.now().minusYears(d))
                        .map(e-> criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"),e))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        //	Criterio por position
        Specification<Workers> specPosition=(root, query, criteriaBuilder)->
                positionId.map(d->criteriaBuilder.equal(root.get("position").get("id"),d))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Workers> specification=Specification.where(specName)
                .and(specSurname)
                .and(specAge)
                .and(specPhone)
                .and(specIsDeleted)
                .and(specAntiquierityMin)
                .and(specAntiquierityMax)
                .and(specPosition);
        return workersCrudRepository.findAll(specification, pageable);
    }

    /**
     * Busca un trabajador por su id
     * @param uuid id del trabajador a buscar
     * @return trabajador encontrado
     * @throws WorkersNotFound si no se encuentra el trabajador
     */
    @Override
    @Cacheable(key="#uuid")
    public Workers findByUUID(UUID uuid) {
        log.info("Buscando Empleado con UUID: " + uuid);
        return workersCrudRepository.findById(uuid).orElseThrow(()->new WorkersNotFound("UUID: "+ uuid));
    }

    /**
     * Crea un trabajador
     * @param worker dto con los datos del trabajador
     * @return trabajador creado
     */
    @Override
    @Cacheable(key="#result.uuid")
    public Workers save(WorkersSaveDto worker) {
        log.info("Guardando Empleado con dni");
        Position position= positionService.findById(worker.getPositionId());
        return workersCrudRepository.save(WorkersMapper.toModel(worker, position));
    }

    /**
     * Actualiza un trabajador
     * @param uuid id del trabajador a actualizar
     * @param worker dto con los datos a actualizar
     * @return trabajador actualizado
     */
    @Override
    @Transactional
    @Cacheable(key="#result.uuid")
    public Workers update(UUID uuid, WorkersUpdateDto worker) {
        log.info("Actualizando Empleado con UUID: " + uuid);
        Workers original= findByUUID(uuid);
        Position position;
        try{
            if(worker.getPositionId()==-1L){
                position=Position.SIN_CATEGORIA;
            }
            else
                position=positionService.findById(worker.getPositionId());
        }catch(NullPointerException e){
            position=Position.SIN_CATEGORIA;
        }

        return workersCrudRepository.save(WorkersMapper.toModel(original,worker, position));
    }

    /**
     * Borra un trabajador
     * @param uuid  id del trabajador a borrar
     */
    @Override
    @Transactional
    public void deleteByUUID(UUID uuid) {
        log.info("Eliminando Empleado con UUID: " + uuid);
        findByUUID(uuid);
        workersCrudRepository.deleteById(uuid);
    }

    /**
     *  Busca un trabajador por su dni
     * @param dni   dni del trabajador a buscar
     * @return trabajador encontrado
     */
    @Override
    @Cacheable(key="#result.uuid")
    public Workers findByDni(String dni) {
        log.info("Buscando empleado con DNI: " + dni);
        return workersCrudRepository.findByDni(dni).orElseThrow(()-> new WorkersNotFound("DNI: " + dni));
    }

    /**
     * Busca todos los trabajadores con isDeleted
     * @param isDeleted indica si los trabajadores buscados estan borrados o no
     * @return Lista de trabajadores encontrados
     */
    @Override
    public List<Workers> findByIsDeleted(Boolean isDeleted) {
        log.info("Buscando Empleados con isDeleted: " + isDeleted);
        return workersCrudRepository.findByIsDeleted(isDeleted);
    }

    /**
     * Actualiza el campo isDeleted a true (elimina lógicamente)
     * @param uuid id del trabajador a actualizar
     */
    @Override
    @Transactional
    public void updateIsDeletedToTrueById(UUID uuid) {
        log.info("Actualizando Empleado con UUID: " + uuid);
        findByUUID(uuid);
        workersCrudRepository.updateIsDeletedToTrueById(uuid);
    }
}
