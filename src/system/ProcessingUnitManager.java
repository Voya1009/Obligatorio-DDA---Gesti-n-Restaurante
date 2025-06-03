package system;

import java.util.List;
import java.util.ArrayList;
import model.*;

public class ProcessingUnitManager {

    private List<ProcessingUnit> units = new ArrayList<>();

    public void addUnit(ProcessingUnit unit) {
        units.add(unit);
    }

    public List<ProcessingUnit> getAllUnits() {
        return units;
    }

    public void submitOrderToUnit(Order o) throws SystemException {
        ProcessingUnit pu = o.getItem().getPU();
        if (pu == null) {
            throw new SystemException("La orden no tiene unidad procesadora asignada.");
        }
        pu.enqueueOrder(o);
    }
}
