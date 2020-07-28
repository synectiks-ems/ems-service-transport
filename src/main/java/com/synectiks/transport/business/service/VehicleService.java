package com.synectiks.transport.business.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.synectiks.transport.config.ApplicationProperties;
import com.synectiks.transport.constant.CmsConstants;
import com.synectiks.transport.domain.*;
import com.synectiks.transport.domain.vo.CmsTransportRouteVo;
import com.synectiks.transport.domain.vo.CmsVehicleListVo;
import com.synectiks.transport.domain.vo.CmsVehicleVo;
import com.synectiks.transport.filter.vehicle.VehicleListFilterInput;
import com.synectiks.transport.graphql.types.TransportRoute.AddTransportRouteInput;
import com.synectiks.transport.graphql.types.Vehicle.AddVehicleInput;
import com.synectiks.transport.repository.*;
import com.synectiks.transport.service.util.CommonUtil;
import com.synectiks.transport.service.util.DateFormatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.synectiks.transport.constant.CmsConstants;
import com.synectiks.transport.service.util.CommonUtil;
import com.synectiks.transport.service.util.DateFormatUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class VehicleService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    VehicleRepository vehicleRepository;

//    @Autowired
//    TransportRouteRepository transportRouteRepository;
//
//    @Autowired
//    ContractRepository contractRepository;

    @Autowired
    VehicleDriverLinkRepository vehicleDriverLinkRepository;

    @Autowired
    VehicleContractLinkRepository vehicleContractLinkRepository;

    @Autowired
    TransportRouteVehicleLinkRepository transportRouteVehicleLinkRepository;

    @Autowired
    CommonService commonService;

    @Autowired
    ApplicationProperties applicationProperties;

    @Autowired
    TransportRouteService transportRouteService;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    StopageService stopageService;

    @Autowired
    ContractService contractService;

    @Autowired
    VehicleContractLinkService vehicleContractLinkService;

    @Autowired
    VehicleDriverLinkService vehicleDriverLinkService;

    @Autowired
    TransportRouteVehicleLinkService transportRouteVehicleLinkService;

    @Autowired
    TransportRouteStopageLinkService transportRouteStopageLinkService;

    @Autowired
    TransportRouteRepository transportRouteRepository;

    public List<CmsVehicleListVo> searchVehicle(Long transportRouteId, Long vehicleId, Long transportRouteVehicleLinkId) throws Exception {
        Vehicle vehicle = new Vehicle();
        TransportRouteVehicleLink tvl = new TransportRouteVehicleLink();
        if(vehicleId != null) {
             vehicle.setId(vehicleId);
        }

        if(transportRouteVehicleLinkId != null) {
            tvl.setId(transportRouteVehicleLinkId);
            tvl.setVehicle(vehicle);
            vehicle.setId(vehicleId);
        }
//        if (vehicleId != null) {
//            Vehicle vehicle = new Vehicle();
//            vehicle.setId(vehicleId);
//            tvl.setVehicle(vehicle);
//        }
//        if (transportRouteId != null) {
//            TransportRoute transportRoute = new TransportRoute();
//            transportRoute.setId(transportRouteId);
//            tvl.setTransportRoute(transportRoute);
//        }
//        VehicleDriverLink vdl = new VehicleDriverLink();
//        if (employeeId !=null){
////            VehicleDriverLink vdl = new VehicleDriverLink();
//            Employee employee = new Employee();
//            employee.setId(employeeId);
//            vdl.setEmployeeId(employeeId);
//        }
//        if (vehicleId != null) {
//            Vehicle vehicle = new Vehicle();
//            vehicle.setId(vehicleId);
//            tvl.setVehicle(vehicle);
//            vdl.setVehicle(vehicle);
//        }
//        if (contractId !=null){
//            Contract contract = new Contract();
//            contract.setId(contractId);
//        }
//        if (insuranceId !=null){
//            Insurance insurance = new Insurance();
//            insurance.setId(insuranceId);
//        }
        Example<Vehicle> example = Example.of(vehicle);
        Example<TransportRouteVehicleLink> ex = Example.of(tvl);
        List<Vehicle> list = this.vehicleRepository.findAll(example);
        List<TransportRouteVehicleLink> list1 = this.transportRouteVehicleLinkRepository.findAll(ex);
        List<CmsVehicleListVo> ls = new ArrayList<>();
        for(Vehicle vehicle1: list) {
            CmsVehicleListVo vo = CommonUtil.createCopyProperties(vehicle1, CmsVehicleListVo.class);
            TransportRoute t = this.transportRouteService.getTransportRoute(transportRouteId);
            vo.getTransportRouteId();
            Vehicle v = this.vehicleService.getVehicle(vehicleId);
            vo.getVehicleId();
            TransportRouteVehicleLink trvl = this.transportRouteVehicleLinkService.getTransportRouteVehicleLink(vehicleId);
            vo.getTransportRouteVehicleLinkId();
//            vo.setStrDateOfRegistration(DateFormatUtil.changeLocalDateFormat(vehicle1.getDateOfRegistration(), CmsConstants.DATE_FORMAT_dd_MM_yyyy));
//            vo.setDateOfRegistration(null);
            ls.add(vo);
        }
        for(TransportRouteVehicleLink transportRouteVehicleLink: list1){
            CmsVehicleListVo vo = CommonUtil.createCopyProperties(transportRouteVehicleLink, CmsVehicleListVo.class);
            TransportRoute t = this.transportRouteService.getTransportRoute(transportRouteId);
            vo.getTransportRouteId();
            Vehicle v = this.vehicleService.getVehicle(vehicleId);
            vo.getVehicleId();
//            TransportRouteVehicleLink trvl = this.transportRouteVehicleLinkService.getTransportRouteVehicleLink(vehicleId);
//            vo.getTransportRouteVehicleLinkId();
//            vo.setStrDateOfRegistration(DateFormatUtil.changeLocalDateFormat(vehicle1.getDateOfRegistration(), CmsConstants.DATE_FORMAT_dd_MM_yyyy));
//            vo.setDateOfRegistration(null);
            ls.add(vo);
        }
        return ls;
    }

    public List<CmsVehicleListVo> searchVehicle(VehicleListFilterInput filter) throws Exception {
        Vehicle vehicle = new Vehicle();
        TransportRouteVehicleLink tvl = new TransportRouteVehicleLink();
        if (!CommonUtil.isNullOrEmpty(filter.getTransportRouteVehicleLinkId())) {
            TransportRouteVehicleLink transportRouteVehicleLink = new TransportRouteVehicleLink();
            if (transportRouteVehicleLink != null) {
                tvl.setId(Long.valueOf(filter.getTransportRouteVehicleLinkId()));
                vehicle.setId(Long.valueOf(filter.getVehicleId()));
            }
        }
        if (!CommonUtil.isNullOrEmpty(filter.getTransportRouteId())) {
            TransportRoute transportRoute = new TransportRoute();
            if (transportRoute != null) {
                tvl.setId(Long.valueOf(filter.getTransportRouteId()));
//                tvl.setTransportRoute(transportRoute);
            }
        }
        if (!CommonUtil.isNullOrEmpty(filter.getVehicleId())) {
//            Vehicle vehicle1 = new Vehicle();
            if (vehicle != null) {
                tvl.setId(Long.valueOf(filter.getVehicleId()));
//                tvl.setId(Long.valueOf(filter.getTransportRouteVehicleLinkId()));
            }
        }
//        VehicleDriverLink vdl = new VehicleDriverLink();
//        if (!CommonUtil.isNullOrEmpty(filter.getEmployeeId())) {
//            vdl.setEmployeeId(Long.parseLong(filter.getEmployeeId()));
//        }
//        if (!CommonUtil.isNullOrEmpty(filter.getVehicleId())) {
//            Vehicle vehicle = new Vehicle();
//            if (vehicle != null) {
//                vdl.setId(Long.valueOf(filter.getVehicleId()));
////                tvl.setVehicle(vehicle);
//            }
//        }
//        if (!CommonUtil.isNullOrEmpty(filter.getVehicleId())) {
//            if (vehicle != null) {
//                vehicle.setId(Long.valueOf(filter.getVehicleId()));
//            }
//        }
//        if (!CommonUtil.isNullOrEmpty(filter.getEmployeeId())) {
//            Employee employee = new Employee();
//            if (employee !=null){
//                employee.setId(Long.valueOf(filter.getEmployeeId()));
//            }
//        }
//        if (!CommonUtil.isNullOrEmpty(filter.getContractId())) {
//            Contract contract = new Contract();
//            if (contract !=null){
//                contract.setId(Long.valueOf(filter.getContractId()));
//            }
//        }
//        if (!CommonUtil.isNullOrEmpty(filter.getInsuranceId())) {
//            Insurance insurance = new Insurance();
//            if (insurance !=null){
//                insurance.setId(Long.valueOf(filter.getInsuranceId()));
//            }
//        }
        Example<Vehicle> example = Example.of(vehicle);

        List<Vehicle> list = this.vehicleRepository.findAll(example);
        Example<TransportRouteVehicleLink> ex = Example.of(tvl);
        List<CmsVehicleListVo> ls = new ArrayList<>();
        List<TransportRouteVehicleLink> list1 = this.transportRouteVehicleLinkRepository.findAll(ex);

        for(Vehicle vehicle1: list) {
            CmsVehicleListVo vo = CommonUtil.createCopyProperties(vehicle1, CmsVehicleListVo.class);
//            String url = transportSrvUrl + "/TransportRoute-by-filters/" + vo.getTransportRoute();
//            TransportRoute tr = this.commonService.getObject(url, TransportRoute.class);
//            vo.getTransportRoute();
//            String url1 = transportSrvUrl + "/api/vehicle-by-id/" + vo.getVehicle();
//            Vehicle v = this.commonService.getObject(url1, Vehicle.class);
//            vo.getVehicleId();
//            vo.setStrDateOfRegistration(DateFormatUtil.changeLocalDateFormat(vehicle1.getDateOfRegistration(), CmsConstants.DATE_FORMAT_dd_MM_yyyy));
//            vo.setDateOfRegistration(null);
//            CmsVehicleListVo tr = new CmsVehicleListVo();
//            if(tr.getTransportRoute() != null) {
//                TransportRoute transportRoute = new TransportRoute();
//                tr.setTransportRouteId(tr.getTransportRoute().getId());
//                tr.setTransportRoute(transportRoute);
////                CmsTransportRouteVo cmsSvo =CommonUtil.createCopyProperties(tr.getTransportRoute(), CmsTransportRouteVo.class);
////                vo.setTransportRoute(cmsSvo);
//            }
            ls.add(vo);
        }
        for(TransportRouteVehicleLink transportRouteVehicleLink: list1){
            CmsVehicleListVo vo = CommonUtil.createCopyProperties(transportRouteVehicleLink, CmsVehicleListVo.class);
//            TransportRoute t = this.transportRouteService.getTransportRoute(transportRouteId);
//            vo.getTransportRouteId();
//            Vehicle v = this.vehicleService.getVehicle(vehicleId);
//            vo.getVehicleId();
//            TransportRouteVehicleLink trvl = this.transportRouteVehicleLinkService.getTransportRouteVehicleLink(vehicleId);
//            vo.getTransportRouteVehicleLinkId();
//            vo.setStrDateOfRegistration(DateFormatUtil.changeLocalDateFormat(vehicle1.getDateOfRegistration(), CmsConstants.DATE_FORMAT_dd_MM_yyyy));
//            vo.setDateOfRegistration(null);
            ls.add(vo);
        }
        return ls;
    }

    public List<CmsVehicleVo> getCmsVehicleListOnFilterCriteria(Map<String, String> criteriaMap) {
        Vehicle obj = new Vehicle();
        boolean isFilter = false;
        if (criteriaMap.get("id") != null) {
            obj.setId(Long.parseLong(criteriaMap.get("id")));
            isFilter = true;
        }
        if (criteriaMap.get("vehicleNumber") != null) {
            obj.setVehicleNumber(criteriaMap.get("vehicleNumber"));
            isFilter = true;
        }
        if (criteriaMap.get("vehicleType") != null) {
            obj.setVehicleType(criteriaMap.get("vehicleType"));
            isFilter = true;
        }
        if (criteriaMap.get("capacity") != null) {
            obj.setCapacity(Long.parseLong(criteriaMap.get("capacity")));
            isFilter = true;
        }
        if (criteriaMap.get("ownerShip") != null) {
            obj.setOwnerShip(criteriaMap.get("ownerShip"));
            isFilter = true;
        }
        if (criteriaMap.get("dateOfRegistration") != null) {
            obj.setDateOfRegistration(DateFormatUtil.convertStringToLocalDate(criteriaMap.get("dateOfRegistration"), CmsConstants.DATE_FORMAT_dd_MM_yyyy));
            isFilter = true;
        }
        if (criteriaMap.get("yearOfManufacturing") != null) {
            obj.setYearOfManufacturing(criteriaMap.get("yearOfManufacturing"));
            isFilter = true;
        }
        if (criteriaMap.get("manufacturingCompany") != null) {
            obj.setManufacturingCompany(criteriaMap.get("manufacturingCompany"));
            isFilter = true;
        }
        if (criteriaMap.get("model") != null) {
            obj.setModel(criteriaMap.get("model"));
            isFilter = true;
        }
        if (criteriaMap.get("chasisNo") != null) {
            obj.setChasisNo(criteriaMap.get("chasisNo"));
            isFilter = true;
        }
        if (criteriaMap.get("rcNo") != null) {
            obj.setRcNo(criteriaMap.get("rcNo"));
            isFilter = true;
        }
        if (criteriaMap.get("status") != null) {
            obj.setStatus(criteriaMap.get("status"));
            isFilter = true;
        }
        if (criteriaMap.get("onBoardingDate") != null) {
            obj.setOnBoardingDate(DateFormatUtil.convertStringToLocalDate(criteriaMap.get("onBoardingDate"), CmsConstants.DATE_FORMAT_dd_MM_yyyy));
            isFilter = true;
        }
        if (criteriaMap.get("branchId") != null) {
            obj.setBranchId(Long.parseLong(criteriaMap.get("branchId")));
            isFilter = true;
        }
        List<Vehicle> list = null;
        if(isFilter) {
            list = this.vehicleRepository.findAll(Example.of(obj), Sort.by(Direction.DESC, "id"));
        }else {
            list = this.vehicleRepository.findAll(Sort.by(Direction.DESC, "id"));
        }
        List<CmsVehicleVo> ls = changeVehicleToCmsVehicleList(list);
        if(ls.size() == 0) {
            return Collections.emptyList();
        }
        Collections.sort(ls, (o1, o2) -> o2.getId().compareTo(o1.getId()));
        return ls;
    }
    public List<Vehicle> getVehicleListOnFilterCriteria(Map<String, String> criteriaMap) {
        Vehicle obj = new Vehicle();
        boolean isFilter = false;
        if (criteriaMap.get("id") != null) {
            obj.setId(Long.parseLong(criteriaMap.get("id")));
            isFilter = true;
        }
        if (criteriaMap.get("vehicleNumber") != null) {
            obj.setVehicleNumber(criteriaMap.get("vehicleNumber"));
            isFilter = true;
        }
        if (criteriaMap.get("vehicleType") != null) {
            obj.setVehicleType(criteriaMap.get("vehicleType"));
            isFilter = true;
        }
        if (criteriaMap.get("capacity") != null) {
            obj.setCapacity(Long.parseLong(criteriaMap.get("capacity")));
            isFilter = true;
        }
        if (criteriaMap.get("ownerShip") != null) {
            obj.setOwnerShip(criteriaMap.get("ownerShip"));
            isFilter = true;
        }
        if (criteriaMap.get("dateOfRegistration") != null) {
            obj.setDateOfRegistration(DateFormatUtil.convertStringToLocalDate(criteriaMap.get("dateOfRegistration"), CmsConstants.DATE_FORMAT_dd_MM_yyyy));
            isFilter = true;
        }
        if (criteriaMap.get("yearOfManufacturing") != null) {
            obj.setYearOfManufacturing(criteriaMap.get("yearOfManufacturing"));
            isFilter = true;
        }
        if (criteriaMap.get("manufacturingCompany") != null) {
            obj.setManufacturingCompany(criteriaMap.get("manufacturingCompany"));
            isFilter = true;
        }
        if (criteriaMap.get("model") != null) {
            obj.setModel(criteriaMap.get("model"));
            isFilter = true;
        }
        if (criteriaMap.get("chasisNo") != null) {
            obj.setChasisNo(criteriaMap.get("chasisNo"));
            isFilter = true;
        }
        if (criteriaMap.get("rcNo") != null) {
            obj.setRcNo(criteriaMap.get("rcNo"));
            isFilter = true;
        }
        if (criteriaMap.get("status") != null) {
            obj.setStatus(criteriaMap.get("status"));
            isFilter = true;
        }
        if (criteriaMap.get("onBoardingDate") != null) {
            obj.setOnBoardingDate(DateFormatUtil.convertStringToLocalDate(criteriaMap.get("onBoardingDate"), CmsConstants.DATE_FORMAT_dd_MM_yyyy));
            isFilter = true;
        }
        if (criteriaMap.get("branchId") != null) {
            obj.setBranchId(Long.parseLong(criteriaMap.get("branchId")));
            isFilter = true;
        }
        List<Vehicle> list = null;
        if(isFilter) {
            list = this.vehicleRepository.findAll(Example.of(obj), Sort.by(Direction.DESC, "id"));
        }else {
            list = this.vehicleRepository.findAll(Sort.by(Direction.DESC, "id"));
        }
        List<CmsVehicleVo> ls = changeVehicleToCmsVehicleList(list);
        if(ls.size() == 0) {
            return Collections.emptyList();
        }
        Collections.sort(list, (o1, o2) -> o2.getId().compareTo(o1.getId()));
        return list;
    }
    public CmsVehicleVo getCmsVehicle(Long id){
        Optional<Vehicle> v = this.vehicleRepository.findById(id);
        if(v.isPresent()) {
            CmsVehicleVo vo = CommonUtil.createCopyProperties(v.get(), CmsVehicleVo.class);
            convertDatesAndProvideDependencies(v.get(), vo);
            logger.debug("CmsVehicle for given id : "+id+". CmsVehicle object : "+vo);
            return vo;
        }
        logger.debug("Vehicle object not found for the given id. "+id+". Returning null object");
        return null;
    }

    public Vehicle getVehicle(Long id){
        Optional< Vehicle> v = this.vehicleRepository.findById(id);
        if(v.isPresent()) {
            return v.get();
        }
        logger.debug("Vehicle object not found for the given id. "+id+". Returning null");
        return null;
    }

    private List<CmsVehicleVo> changeVehicleToCmsVehicleList(List<Vehicle> list){
        List<CmsVehicleVo> ls = new ArrayList<>();
        for(Vehicle v: list) {
            CmsVehicleVo vo = CommonUtil.createCopyProperties(v, CmsVehicleVo.class);
            convertDatesAndProvideDependencies(v, vo);
            ls.add(vo);
        }
        return ls;
    }
    private void convertDatesAndProvideDependencies(Vehicle v, CmsVehicleVo vo) {
        if(v.getDateOfRegistration() != null) {
            vo.setStrDateOfRegistration(DateFormatUtil.changeLocalDateFormat(v.getDateOfRegistration(), CmsConstants.DATE_FORMAT_dd_MM_yyyy));
            vo.setDateOfRegistration(null);
        }
        if(v.getOnBoardingDate() != null) {
            vo.setStrOnBoardingDate(DateFormatUtil.changeLocalDateFormat(v.getOnBoardingDate(), CmsConstants.DATE_FORMAT_dd_MM_yyyy));
            vo.setOnBoardingDate(null);
        }
    }

//        public List<CmsVehicleVo> searchVehicle(Long transportRouteId, Long employeeId, Long vehicleId, String vehicleNumber) throws Exception {
//        Vehicle vehicle = new Vehicle();
//        if (vehicleId != null) {
//            vehicle.setId(vehicleId);
//        }
////        if (employeeId !=null){
////            vehicle.setEmployeeId(employeeId);
////        }
////        if (transportRouteId != null) {
////            TransportRoute transportRoute = new TransportRoute();
////            transportRoute.setId(transportRouteId);
////            vehicle.setTransportRoute(transportRoute);
////        }
//        if (vehicleNumber != null) {
//            vehicle.setVehicleNumber(vehicleNumber);
//        }
//        Example<Vehicle> example = Example.of(vehicle);
//        List<Vehicle> list = this.vehicleRepository.findAll(example);
//        List<CmsVehicleVo> ls = new ArrayList<>();
//        for(Vehicle vehicle1: list) {
//            CmsVehicleVo vo = CommonUtil.createCopyProperties(vehicle1, CmsVehicleVo.class);
//            vo.setStrDateOfRegistration(DateFormatUtil.changeLocalDateFormat(vehicle1.getDateOfRegistration(), CmsConstants.DATE_FORMAT_dd_MM_yyyy));
//            vo.setStrOnBoardingDate(DateFormatUtil.changeLocalDateFormat(vehicle1.getOnBoardingDate(), CmsConstants.DATE_FORMAT_dd_MM_yyyy));
//            vo.setStrOnBoardingDate(null);
//            vo.setDateOfRegistration(null);
//            ls.add(vo);
//        }
//        return ls;
//    }
    public List<Vehicle> getVehicleList(Long branchId) {
        List<Vehicle> list = null;
        if(branchId != null) {
            Vehicle vl = new Vehicle();
            vl.setBranchId(branchId);
            list = this.vehicleRepository.findAll(Example.of(vl));
        }else {
            list = this.vehicleRepository.findAll();
        }
        Collections.sort(list, (o1, o2) -> o2.getId().compareTo(o1.getId()));
        return list;
    }

//    public List<CmsVehicleVo> getCmsVehicleList(Status status) {
//        Vehicle vehicle = new Vehicle();
//        vehicle.setStatus(status);
//        List<Vehicle> list = this.vehicleRepository.findAll(Example.of(vehicle));
//        List<CmsVehicleVo> ls = changeVehicleToCmsVehicleList(list);
//        Collections.sort(ls, (o1, o2) -> o2.getId().compareTo(o1.getId()));
//        return ls;
//    }
//
//    public List<Vehicle> getVehicleList(Status status) {
//        Vehicle vehicle = new  Vehicle();
//        vehicle.setStatus(status);
//        List<Vehicle> list = this.vehicleRepository.findAll(Example.of(vehicle));
//        Collections.sort(list, (o1, o2) -> o2.getId().compareTo(o1.getId()));
//        return list;
//    }

    public List<VehicleDriverLink> getDriverList(CmsVehicleVo vo){
        VehicleDriverLink vehicleDriverLink = new VehicleDriverLink();
        Vehicle vehicle = CommonUtil.createCopyProperties(vo, Vehicle.class);
        vehicleDriverLink.setVehicle(vehicle);
        List<VehicleDriverLink> list = this.vehicleDriverLinkRepository.findAll(Example.of(vehicleDriverLink));
//    Collections.sort(list, (o1, o2) -> o1.getFacility().getName().compareTo(o2.getFacility().getName()));
        return list;
    }

    public List<VehicleContractLink> getVehicleContractList(CmsVehicleVo vo){
        VehicleContractLink vehicleContractLink = new VehicleContractLink();
        Vehicle vehicle = CommonUtil.createCopyProperties(vo, Vehicle.class);
        vehicleContractLink.setVehicle(vehicle);
        List<VehicleContractLink> list = this.vehicleContractLinkRepository.findAll(Example.of(vehicleContractLink));
//    Collections.sort(list, (o1, o2) -> o1.getFacility().getName().compareTo(o2.getFacility().getName()));
        return list;
    }


    public List<TransportRouteVehicleLink> getRouteVehicleList(CmsVehicleVo vo){
        TransportRouteVehicleLink transportRouteVehicleLink = new TransportRouteVehicleLink();
        Vehicle vehicle = CommonUtil.createCopyProperties(vo, Vehicle.class);
        transportRouteVehicleLink.setVehicle(vehicle);
        List<TransportRouteVehicleLink> list = this.transportRouteVehicleLinkRepository.findAll(Example.of(transportRouteVehicleLink));
//    Collections.sort(list, (o1, o2) -> o1.getFacility().getName().compareTo(o2.getFacility().getName()));
        return list;
    }
    public List<CmsVehicleVo> getVehicleList(){
        List<Vehicle> list = this.vehicleRepository.findAll();
        List<CmsVehicleVo> ls = changeVehicleToCmsVehicleList(list);
        Collections.sort(ls, (o1, o2) -> o2.getId().compareTo(o1.getId()));
        return ls;
    }

    public CmsVehicleVo addVehicle(AddVehicleInput input) {
        logger.info("Saving vehicle");
        CmsVehicleVo vo = null;
        try {
            Vehicle vehicle = null;
            if (input.getId() == null) {
                logger.debug("Adding new Vehicle");
                vehicle = CommonUtil.createCopyProperties(input, Vehicle.class);
            } else {
                logger.debug("Updating existing Vehicle");
                vehicle = this.vehicleRepository.findById(input.getId()).get();
            }
//            TransportRoute tr = this.transportRouteRepository.findById(input.getTransportRouteId()).get();
//            vehicle.setTransportRoute(tr);
//            Contract ct = this.contractRepository.findById(input.getContractId()).get();
//            vehicle.setContract(ct);
            if(input.getVehicleNumber() != null) {
                vehicle.setVehicleNumber(input.getVehicleNumber());
            }
            if(input.getVehicleNumber() != null) {
                vehicle.setVehicleType(input.getVehicleType());
            }
            vehicle.setOwnerShip(input.getOwnerShip());
            vehicle.setCapacity(input.getCapacity());
            vehicle.setYearOfManufacturing(input.getYearOfManufacturing());
            vehicle.setManufacturingCompany(input.getManufacturingCompany());
            vehicle.setChasisNo(input.getChasisNo());
            vehicle.setRcNo(input.getRcNo());
            vehicle.setModel(input.getModel());
            vehicle.setStatus(input.getStatus());
//            vehicle.setEmployeeId(input.getEmployeeId());
            vehicle.setBranchId(input.getBranchId());

            String prefUrl = applicationProperties.getPrefSrvUrl();
            if (input.getBranchId() != null) {
                String url = prefUrl + "/api/branch-by-id/" + input.getBranchId();
                Branch branch = this.commonService.getObject(url, Branch.class);
                if (branch != null) {
                    vehicle.setBranchName(branch.getBranchName());
                }
            }
//            vehicle.setCollegeId(input.getCollegeId());
            vehicle.setDateOfRegistration(input.getStrDateOfRegistration() != null ? DateFormatUtil.convertStringToLocalDate(input.getStrDateOfRegistration(), CmsConstants.DATE_FORMAT_dd_MM_yyyy) : null);
           vehicle.setOnBoardingDate(input.getStrOnBoardingDate() != null ? DateFormatUtil.convertStringToLocalDate(input.getStrOnBoardingDate(), CmsConstants.DATE_FORMAT_dd_MM_yyyy) : null);
            vehicle = this.vehicleRepository.save(vehicle);
            vo = CommonUtil.createCopyProperties(vehicle, CmsVehicleVo.class);
            vo.setStrDateOfRegistration(vehicle.getDateOfRegistration() != null ? DateFormatUtil.changeLocalDateFormat(vehicle.getDateOfRegistration(), CmsConstants.DATE_FORMAT_dd_MM_yyyy) : "");
           vo.setStrOnBoardingDate(vehicle.getOnBoardingDate() != null ? DateFormatUtil.changeLocalDateFormat(vehicle.getOnBoardingDate(), CmsConstants.DATE_FORMAT_dd_MM_yyyy) : "");
            vo.setCreatedOn(null);
            vo.setUpdatedOn(null);
            vo.setExitCode(0L);
            if (input.getId() == null) {
                vo.setExitDescription("vehicle is added successfully");
                logger.debug("vehicle is added successfully");
            } else {
                vo.setExitDescription("vehicle is updated successfully");
                logger.debug("vehicle is updated successfully");
            }

        } catch (Exception e) {
            vo = new CmsVehicleVo();
            vo.setExitCode(1L);
            vo.setExitDescription("Due to some exception, vehicle data cannot be saved");
            logger.error("Vehicle save failed. Exception : ", e);
        }
        logger.info("Vehicle saved successfully");
        List<CmsVehicleVo> ls = getVehicleList();
        vo.setDataList(ls);
        return vo;
    }


//    public void saveTransportVehicleMapping(AddVehicleInput input, CmsVehicleVo vo) {
//        TransportRouteVehicleLink transportRouteVehicleLink = this.transportRouteVehicleLinkService.saveTransportRouteVehicleLink(input);
//        VehicleDriverLink vehicleDriverLink = this.vehicleDriverLinkService.saveVehicleDriverLink(input);
//        VehicleContractLink vehicleContractLink = this.vehicleContractLinkService.saveVehicleContractLink(input);
//        providedVehicleDependencies(transportRoute, vo);
//        TransportRoute transportRoute = this.transportRouteService.getTransportRoute(input.getTransportRouteId());
//        Contract contract = this.contractService.getContract(input.getContractId());

//    }
}





