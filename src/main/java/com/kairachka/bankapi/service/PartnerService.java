package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.Partner;
import com.kairachka.bankapi.exception.PartnerNotFoundException;
import com.kairachka.bankapi.mapper.PartnerMapper;
import com.kairachka.bankapi.repository.PartnerRepository;
import com.sun.net.httpserver.HttpExchange;

import java.util.List;

public class PartnerService {
    PartnerRepository partnerRepository = new PartnerRepository();
    PartnerMapper partnerMapper = new PartnerMapper();

    public boolean addPartner(HttpExchange exchange) {
        Partner partner = partnerMapper.JsonToPartner(exchange);
        return partnerRepository.addPartner(partner);
    }

    public List<Partner> getAllPartners() {
        List<Partner> partnerList = partnerRepository.getAllPartners();
        if(!partnerList.isEmpty()) {
            return partnerList;
        } else {
            throw new PartnerNotFoundException("Partner not found exception");
        }
    }
}
