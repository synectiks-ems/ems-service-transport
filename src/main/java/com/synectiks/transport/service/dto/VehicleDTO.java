package com.synectiks.transport.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.synectiks.transport.domain.Vehicle} entity.
 */
public class VehicleDTO implements Serializable {

    private Long id;

    private String vehicleNumber;

    private String vehicleType;

    private Long capacity;

    private String ownerShip;

    private LocalDate dateOfRegistration;

    private String yearOfManufacturing;

    private String manufacturingCompany;

    private String model;

    private String chasisNo;

    private String rcNo;

    private String status;

    private LocalDate onBoardingDate;

    private Long branchId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public String getOwnerShip() {
        return ownerShip;
    }

    public void setOwnerShip(String ownerShip) {
        this.ownerShip = ownerShip;
    }

    public LocalDate getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(LocalDate dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public String getYearOfManufacturing() {
        return yearOfManufacturing;
    }

    public void setYearOfManufacturing(String yearOfManufacturing) {
        this.yearOfManufacturing = yearOfManufacturing;
    }

    public String getManufacturingCompany() {
        return manufacturingCompany;
    }

    public void setManufacturingCompany(String manufacturingCompany) {
        this.manufacturingCompany = manufacturingCompany;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getChasisNo() {
        return chasisNo;
    }

    public void setChasisNo(String chasisNo) {
        this.chasisNo = chasisNo;
    }

    public String getRcNo() {
        return rcNo;
    }

    public void setRcNo(String rcNo) {
        this.rcNo = rcNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getOnBoardingDate() {
        return onBoardingDate;
    }

    public void setOnBoardingDate(LocalDate onBoardingDate) {
        this.onBoardingDate = onBoardingDate;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VehicleDTO vehicleDTO = (VehicleDTO) o;
        if (vehicleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vehicleDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VehicleDTO{" +
            "id=" + getId() +
            ", vehicleNumber='" + getVehicleNumber() + "'" +
            ", vehicleType='" + getVehicleType() + "'" +
            ", capacity=" + getCapacity() +
            ", ownerShip='" + getOwnerShip() + "'" +
            ", dateOfRegistration='" + getDateOfRegistration() + "'" +
            ", yearOfManufacturing='" + getYearOfManufacturing() + "'" +
            ", manufacturingCompany='" + getManufacturingCompany() + "'" +
            ", model='" + getModel() + "'" +
            ", chasisNo='" + getChasisNo() + "'" +
            ", rcNo='" + getRcNo() + "'" +
            ", status='" + getStatus() + "'" +
            ", onBoardingDate='" + getOnBoardingDate() + "'" +
            ", branchId=" + getBranchId() +
            "}";
    }
}
