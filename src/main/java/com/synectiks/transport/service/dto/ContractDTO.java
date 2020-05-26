package com.synectiks.transport.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.synectiks.transport.domain.Contract} entity.
 */
public class ContractDTO implements Serializable {

    private Long id;

    private String vendorName;

    private String typeOfOwnerShip;

    private String durationOfContract;

    private LocalDate startDate;

    private LocalDate endDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getTypeOfOwnerShip() {
        return typeOfOwnerShip;
    }

    public void setTypeOfOwnerShip(String typeOfOwnerShip) {
        this.typeOfOwnerShip = typeOfOwnerShip;
    }

    public String getDurationOfContract() {
        return durationOfContract;
    }

    public void setDurationOfContract(String durationOfContract) {
        this.durationOfContract = durationOfContract;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContractDTO contractDTO = (ContractDTO) o;
        if (contractDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contractDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ContractDTO{" +
            "id=" + getId() +
            ", vendorName='" + getVendorName() + "'" +
            ", typeOfOwnerShip='" + getTypeOfOwnerShip() + "'" +
            ", durationOfContract='" + getDurationOfContract() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
