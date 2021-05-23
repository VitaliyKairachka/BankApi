package com.kairachka.bankapi.service.Impl;

import com.kairachka.bankapi.entity.Partner;
import com.kairachka.bankapi.exception.PartnerNotFoundException;
import com.kairachka.bankapi.repository.Impl.PartnerRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PartnerServiceImplTest {
    @Mock
    PartnerRepositoryImpl partnerRepository = Mockito.mock(PartnerRepositoryImpl.class);

    @InjectMocks
    PartnerServiceImpl partnerService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addPartner() {
        Mockito.when(partnerRepository.addPartner(Mockito.any())).thenReturn(true);
        assertTrue(partnerService.addPartner(new Partner()));
    }

    @Test
    void addPartnerFalse() {
        Mockito.when(partnerRepository.addPartner(Mockito.any())).thenReturn(false);
        assertFalse(partnerService.addPartner(new Partner()));
    }

    @Test
    void getAllPartners() throws PartnerNotFoundException {
        List<Partner> partnerList = new ArrayList<>();
        partnerList.add(new Partner());
        partnerList.add(new Partner());
        Mockito.when(partnerRepository.getAllPartners()).thenReturn(partnerList);
        assertEquals(partnerList, partnerService.getAllPartners());
    }

    @Test
    void testGetAllPartners() {
        List<Partner> partnerList = new ArrayList<>();
        Mockito.when(partnerRepository.getAllPartners()).thenReturn(partnerList);
        assertThrows(PartnerNotFoundException.class, () -> partnerService.getAllPartners());
    }
}