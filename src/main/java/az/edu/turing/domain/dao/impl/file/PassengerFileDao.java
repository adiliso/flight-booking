package az.edu.turing.domain.dao.impl.file;

import az.edu.turing.domain.dao.abstracts.PassengerDao;
import az.edu.turing.domain.entity.PassengerEntity;
import az.edu.turing.util.FileDaoUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PassengerFileDao extends PassengerDao {

    private static final FileDaoUtil<PassengerEntity> FILE_DAO_UTIL = new FileDaoUtil<>(System.getenv("PASSENGERS_FILE"));
    private static final List<PassengerEntity> PASSENGERS = FILE_DAO_UTIL.readObject();

    @Override
    public List<PassengerEntity> findAll() {
        return new ArrayList<>(PASSENGERS);
    }

    @Override
    public PassengerEntity create(PassengerEntity passengerEntity) {
        Optional<PassengerEntity> passenger = findByNameAndLastName(
                passengerEntity.getName(), passengerEntity.getLastName());
        if (passenger.isPresent()) {
            return passenger.get();
        }
        passengerEntity.setId(idGenerator());
        PASSENGERS.add(passengerEntity);
        return passengerEntity;
    }

    @Override
    public Optional<PassengerEntity> getById(Long id) {
        return PASSENGERS
                .stream()
                .filter(passenger -> passenger.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<PassengerEntity> findByNameAndLastName(String name, String lastName) {
        return PASSENGERS
                .stream()
                .filter(p -> p.getName().equals(name) && p.getLastName().equals(lastName))
                .findFirst();
    }

    @Override
    public void saveChanges() {
        FILE_DAO_UTIL.writeObject(PASSENGERS);
    }

    private long idGenerator() {
        if (PASSENGERS.isEmpty()) {
            return 1;
        }
        return PASSENGERS.get(PASSENGERS.size() - 1).getId() + 1;
    }
}
