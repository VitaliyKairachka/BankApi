package com.kairachka.bankapi.service;

import com.kairachka.bankapi.entity.Partner;
import com.sun.net.httpserver.HttpExchange;

import java.util.List;

public interface PartnerService {
    boolean addPartner(HttpExchange exchange);

    List<Partner> getAllPartners();
}
