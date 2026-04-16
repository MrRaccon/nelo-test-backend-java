package mx.com.test.nelo.alexis.dto;

import java.util.List;

public class TableCombinationDto {
    
    private List<Long> tableIds;
    private List<Integer> tableCapacities;
    private Integer totalCapacity;
    
    // Constructors
    public TableCombinationDto() {}
    
    public TableCombinationDto(List<Long> tableIds, List<Integer> tableCapacities, Integer totalCapacity) {
        this.tableIds = tableIds;
        this.tableCapacities = tableCapacities;
        this.totalCapacity = totalCapacity;
    }
    
    // Getters and Setters
    public List<Long> getTableIds() {
        return tableIds;
    }
    
    public void setTableIds(List<Long> tableIds) {
        this.tableIds = tableIds;
    }
    
    public List<Integer> getTableCapacities() {
        return tableCapacities;
    }
    
    public void setTableCapacities(List<Integer> tableCapacities) {
        this.tableCapacities = tableCapacities;
    }
    
    public Integer getTotalCapacity() {
        return totalCapacity;
    }
    
    public void setTotalCapacity(Integer totalCapacity) {
        this.totalCapacity = totalCapacity;
    }
}
