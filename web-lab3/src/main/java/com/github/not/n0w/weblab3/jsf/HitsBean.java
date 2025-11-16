package com.github.not.n0w.weblab3.jsf;

import com.github.not.n0w.weblab3.model.Hit;
import com.github.not.n0w.weblab3.repository.HitRepository;
import com.github.not.n0w.weblab3.service.HitService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ManagedBean(name = "hitsBean")
@SessionScoped
public class HitsBean implements Serializable {

    @Inject
    private HitService hitService;

    private String sessionId;

    private List<Hit> hits;

    public HitsBean() {}

    @PostConstruct
    public void init() {
        this.sessionId = UUID.randomUUID().toString();
        hits = new ArrayList<>();
    }
    public void clear() {
        hitService.clearHits(this.sessionId);
        hits = new ArrayList<>();
    }


    public String generateHitsAsJson() {

        return "[" + hits.stream()
                .map(Hit::toJson)
                .collect(Collectors.joining(",")) + "]";
    }
    public String formatCell(String content, int maxLength, int maxVisible, int minEnd) {
        if (content == null) return "";
        if (content.length() > maxLength) {
            String start = content.substring(0, Math.min(maxVisible, content.length()));
            String end = content.substring(Math.max(content.length() - minEnd, 0));
            return start + "..." + end;
        }
        return content;
    }

    public String formatCell(String content) {
        return formatCell(content, 7, 5, 2);
    }


    public List<Hit> getHits() {
        return hits;
    }

    public void setHits(List<Hit> hits) {
        this.hits = hits;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }
    public void setHitService(HitService hitService) {
        this.hitService = hitService;
    }

    public HitService getHitService() {
        return hitService;
    }
}
