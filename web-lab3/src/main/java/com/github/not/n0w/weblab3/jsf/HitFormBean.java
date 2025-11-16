package com.github.not.n0w.weblab3.jsf;

import com.github.not.n0w.weblab3.HitsEndpoint;
import com.github.not.n0w.weblab3.model.XCheckbox;
import com.github.not.n0w.weblab3.service.HitService;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;


@ManagedBean(name="hitFormBean")
@SessionScoped
public class HitFormBean implements Serializable {

    @ManagedProperty("#{hitsBean}")
    private HitsBean hitsBean;

    @Inject
    private HitService hitService;

    private List<String> radiusList;
    private List<XCheckbox> xList = new ArrayList<>();
    private String yCoordinate;
    private String radius;


    public HitFormBean() {}

    @PostConstruct
    public void init() {
        radiusList = List.of("1", "1.5", "2", "2.5", "3");
        radius = radiusList.get(0);

        int[] xValues = {-5,-4,-3, -2, -1, 0, 1, 2, 3, 4, 5};
        for (int x : xValues) {
            xList.add(new XCheckbox(String.valueOf(x), false));
        }
        hitService.registerSessionBean(this, hitsBean.getSessionId());
    }

    public void update() {
        hitsBean.setHits(hitService.getHits(hitsBean.getSessionId()));
        String json = hitsBean.generateHitsAsJson();
        HitsEndpoint.send(json);
    }

    public String getGlobalXAsJs() {
        if (xList == null || xList.isEmpty()) {
            return "[]";
        }

        String result = xList.stream()
                .filter(XCheckbox::isSelected)
                .map(XCheckbox::getValue)
                .collect(Collectors.joining(",", "[", "]"));

        return result.isEmpty() ? "[]" : result;
    }
    public String getGlobalYAsJs() {
        return yCoordinate != null ? "\"" + yCoordinate + "\"" : "null";
    }
    public String getGlobalRAsJs() {
        return radius != null ? "\"" + radius + "\"" : "null";
    }

    public void handleCanvasClick() {
        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap();

        String x = params.get("x");
        String y = params.get("y");
        String r = params.get("r");

        System.out.println("Canvas click: x=" + x + ", y=" + y + ", r=" + r);

        hitService.hit(new BigDecimal(x), new BigDecimal(y), new BigDecimal(r), hitsBean.getSessionId());
        hitsBean.setHits(hitService.getHits(hitsBean.getSessionId()));

        String json = hitsBean.generateHitsAsJson();
        String escapedJson = json.replace("\"", "\\\"");
        PrimeFaces.current().executeScript("updateHits(JSON.parse(\"" + escapedJson + "\"));");
    }
    public void onXChanged() {
        String globalXJs = getGlobalXAsJs();

        PrimeFaces.current().executeScript(
                "updateGlobalValues(" + globalXJs + ", globalY, globalR);"
        );
    }
    public void submit() {

        FacesContext context = FacesContext.getCurrentInstance();

        boolean anySelected = xList.stream().anyMatch(XCheckbox::isSelected);
        if (!anySelected) {
            context.addMessage("hit-form:xListHidden",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "At least one X must be selected", null));
            context.validationFailed();
            return;
        }

        for (XCheckbox item : xList) {
            if (!item.isSelected()) continue;
            hitService.hit(
                    new BigDecimal(item.getValue()),
                    new BigDecimal(yCoordinate),
                    new BigDecimal(radius),
                    hitsBean.getSessionId()
            );
        }
        update();
    }



    public void setHitService(HitService hitService) {
        this.hitService = hitService;
    }

    public HitService getHitService() {
        return hitService;
    }

    public void setRadiusList(List<String> radiusList) {
        this.radiusList = radiusList;
    }

    public List<String> getRadiusList() {
        return radiusList;
    }

    public void setyCoordinate(String yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getRadius() {
        return radius;
    }

    public String getyCoordinate() {
        return yCoordinate;
    }

    public void setxList(List<XCheckbox> xList) {
        this.xList = xList;
    }

    public List<XCheckbox> getxList() {
        return xList;
    }

    public void setHitsBean(HitsBean hitsBean) {
        this.hitsBean = hitsBean;
    }

    public HitsBean getHitsBean() {
        return hitsBean;
    }
}
