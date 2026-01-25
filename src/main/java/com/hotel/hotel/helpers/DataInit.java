package com.hotel.hotel.helpers;

import com.hotel.hotel.core.departamento.model.Departamento;
import com.hotel.hotel.core.departamento.repository.DepartamentoRepository;
import com.hotel.hotel.core.habitacion.model.Habitacion;
import com.hotel.hotel.core.habitacion.repository.HabitacionRepository;
import com.hotel.hotel.core.hotel.model.Hotel;
import com.hotel.hotel.core.hotel.repository.HotelRepository;
import com.hotel.hotel.core.tipoHabitacion.model.TipoHabitacion;
import com.hotel.hotel.core.tipoHabitacion.repository.TipoHabitacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInit implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInit.class);

    private final DepartamentoRepository departamentoRepository;
    private final TipoHabitacionRepository tipoHabitacionRepository;
    private final HotelRepository hotelRepository;
    private final HabitacionRepository habitacionRepository;

    public DataInit(DepartamentoRepository departamentoRepository,
                    TipoHabitacionRepository tipoHabitacionRepository,
                    HotelRepository hotelRepository,
                    HabitacionRepository habitacionRepository) {
        this.departamentoRepository = departamentoRepository;
        this.tipoHabitacionRepository = tipoHabitacionRepository;
        this.hotelRepository = hotelRepository;
        this.habitacionRepository = habitacionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        initTiposHabitacion();
        initDepartamentos();
        initHotelesYHabitaciones();
    }

    private void initTiposHabitacion() {
        if (tipoHabitacionRepository.count() == 0) {
            LOGGER.info("[DATA-INIT] Creando tipos de habitacion...");

            TipoHabitacion simple = new TipoHabitacion();
            simple.setNombre("Simple");
            simple.setDescripcion("Habitacion individual con cama simple");
            simple.setCapacidad(1);
            tipoHabitacionRepository.save(simple);

            TipoHabitacion doble = new TipoHabitacion();
            doble.setNombre("Doble");
            doble.setDescripcion("Habitacion con cama doble para 2 personas");
            doble.setCapacidad(2);
            tipoHabitacionRepository.save(doble);

            TipoHabitacion suite = new TipoHabitacion();
            suite.setNombre("Suite");
            suite.setDescripcion("Suite de lujo con sala de estar");
            suite.setCapacidad(4);
            tipoHabitacionRepository.save(suite);

            TipoHabitacion familiar = new TipoHabitacion();
            familiar.setNombre("Familiar");
            familiar.setDescripcion("Habitacion familiar con multiples camas");
            familiar.setCapacidad(5);
            tipoHabitacionRepository.save(familiar);

            LOGGER.info("[DATA-INIT] Tipos de habitacion creados");
        }
    }

    private void initDepartamentos() {
        if (departamentoRepository.count() == 0) {
            LOGGER.info("[DATA-INIT] Creando departamentos...");

            Departamento lima = new Departamento();
            lima.setNombre("Lima");
            lima.setDetalle("Capital del Peru, costa central");
            departamentoRepository.save(lima);

            Departamento cusco = new Departamento();
            cusco.setNombre("Cusco");
            cusco.setDetalle("Ciudad imperial, capital historica del Peru");
            departamentoRepository.save(cusco);

            Departamento arequipa = new Departamento();
            arequipa.setNombre("Arequipa");
            arequipa.setDetalle("Ciudad Blanca, sur del Peru");
            departamentoRepository.save(arequipa);

            LOGGER.info("[DATA-INIT] Departamentos creados");
        }
    }

    private void initHotelesYHabitaciones() {
        if (hotelRepository.count() == 0) {
            LOGGER.info("[DATA-INIT] Creando hoteles y habitaciones...");

            Departamento lima = departamentoRepository.findByNombre("Lima").orElse(null);
            Departamento cusco = departamentoRepository.findByNombre("Cusco").orElse(null);
            Departamento arequipa = departamentoRepository.findByNombre("Arequipa").orElse(null);

            TipoHabitacion simple = tipoHabitacionRepository.findByNombre("Simple").orElse(null);
            TipoHabitacion doble = tipoHabitacionRepository.findByNombre("Doble").orElse(null);
            TipoHabitacion suite = tipoHabitacionRepository.findByNombre("Suite").orElse(null);

            if (lima != null && simple != null && doble != null && suite != null) {
                // Hotel Lima Centro
                Hotel hotelLima = new Hotel();
                hotelLima.setNombre("Hotel Lima Centro");
                hotelLima.setDireccion("Av. Arequipa 1234, Miraflores");
                hotelLima.setDescripcion("Hotel moderno en el corazon de Miraflores");
                hotelLima.setTelefono("01-4567890");
                hotelLima.setEmail("reservas@hotellima.com");
                hotelLima.setDepartamento(lima);
                hotelRepository.save(hotelLima);

                crearHabitacion("101", simple, hotelLima, 80.0);
                crearHabitacion("102", simple, hotelLima, 80.0);
                crearHabitacion("201", doble, hotelLima, 120.0);
                crearHabitacion("202", doble, hotelLima, 120.0);
                crearHabitacion("301", suite, hotelLima, 250.0);
            }

            if (cusco != null && simple != null && doble != null && suite != null) {
                // Hotel Cusco Imperial
                Hotel hotelCusco = new Hotel();
                hotelCusco.setNombre("Hotel Cusco Imperial");
                hotelCusco.setDireccion("Plaza de Armas 567, Cusco");
                hotelCusco.setDescripcion("Hotel colonial con vista a la Plaza de Armas");
                hotelCusco.setTelefono("084-234567");
                hotelCusco.setEmail("reservas@hotelcusco.com");
                hotelCusco.setDepartamento(cusco);
                hotelRepository.save(hotelCusco);

                crearHabitacion("101", simple, hotelCusco, 90.0);
                crearHabitacion("102", doble, hotelCusco, 140.0);
                crearHabitacion("201", doble, hotelCusco, 140.0);
                crearHabitacion("301", suite, hotelCusco, 300.0);
            }

            if (arequipa != null && simple != null && doble != null && suite != null) {
                // Hotel Arequipa Plaza
                Hotel hotelArequipa = new Hotel();
                hotelArequipa.setNombre("Hotel Arequipa Plaza");
                hotelArequipa.setDireccion("Calle Mercaderes 123, Arequipa");
                hotelArequipa.setDescripcion("Hotel boutique en el centro historico");
                hotelArequipa.setTelefono("054-345678");
                hotelArequipa.setEmail("reservas@hotelarequipa.com");
                hotelArequipa.setDepartamento(arequipa);
                hotelRepository.save(hotelArequipa);

                crearHabitacion("101", simple, hotelArequipa, 70.0);
                crearHabitacion("102", simple, hotelArequipa, 70.0);
                crearHabitacion("201", doble, hotelArequipa, 110.0);
                crearHabitacion("301", suite, hotelArequipa, 220.0);
            }

            LOGGER.info("[DATA-INIT] Hoteles y habitaciones creados");
        }
    }

    private void crearHabitacion(String numero, TipoHabitacion tipo, Hotel hotel, double precio) {
        Habitacion habitacion = new Habitacion();
        habitacion.setNumero(numero);
        habitacion.setTipoHabitacion(tipo);
        habitacion.setHotel(hotel);
        habitacion.setPrecio(precio);
        habitacion.setEstado("DISPONIBLE");
        habitacion.setCapacidad(tipo.getCapacidad());
        habitacionRepository.save(habitacion);
    }
}
