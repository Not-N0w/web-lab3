package com.github.not.n0w.weblab3.service;

import com.github.not.n0w.weblab3.WsEndpoint;
import com.github.not.n0w.weblab3.jsf.HitFormBean;
import com.github.not.n0w.weblab3.jsf.HitsBean;
import com.github.not.n0w.weblab3.model.Hit;
import com.github.not.n0w.weblab3.repository.HitRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@ApplicationScoped
public class HitService {

    @Inject
    private HitRepository hitRepository;

    @Inject
    private AreaCheckService areaCheckService;

    @Inject
    private TransmitterService transmitterService;

    private final Map<String, HitFormBean> sessionBeans;

    public HitService() {
        this.sessionBeans = new HashMap<String, HitFormBean>();
    }

    public void registerSessionBean(HitFormBean hitFormBean, String sessionId) {
        sessionBeans.put(sessionId, hitFormBean);
    }
    public HitFormBean getSessionBean(String sessionId) {
        return sessionBeans.get(sessionId);
    }

    public void updateBySession(String sessionId) {
        sessionBeans.get(sessionId).update();
    }

    public Hit hit(BigDecimal x, BigDecimal y, BigDecimal r, String sessionId) {

        var result = hitSilent(x, y, r, sessionId);

        transmitterService.sendDataToBot(
                Map.of(
                        "message", "update",
                        "hit", result
                ),
                sessionId
        );
        return result;
    }


    public void removeSessionBean(String sessionId) {
        sessionBeans.remove(sessionId);
    }
    public Hit hitSilent(BigDecimal x, BigDecimal y, BigDecimal r, String sessionId) {
        long timestamp = System.nanoTime();

        Hit hit = new Hit();
        hit.setSessionId(sessionId);
        hit.setX(x);
        hit.setY(y);
        hit.setR(r);
        hit.setHit(areaCheckService.hit(hit.getX(), hit.getY(), hit.getR()));
        hit.setCurrentDatetime(new Date());
        hit.setExecutionTime((System.nanoTime() - timestamp) / 1_000_000_000.0);

        return hitRepository.save(hit);
    }
    public void sync(String oldSessionId, String newSessionId) {
        hitRepository.sync(oldSessionId, newSessionId);
    }

    public List<Hit> getHits(String sessionId) {
        return hitRepository.getAllBySessionId(sessionId);
    }

    public void clearHits(String sessionId) {
        hitRepository.clearAllBySessionId(sessionId);
    }

    public void setAreaCheckService(AreaCheckService areaCheckService) {
        this.areaCheckService = areaCheckService;
    }

    public void setHitRepository(HitRepository hitRepository) {
        this.hitRepository = hitRepository;
    }

    public HitRepository getHitRepository() {
        return hitRepository;
    }

    public AreaCheckService getAreaCheckService() {
        return areaCheckService;
    }
}
