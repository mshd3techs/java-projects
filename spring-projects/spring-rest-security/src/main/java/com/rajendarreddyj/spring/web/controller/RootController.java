package com.rajendarreddyj.spring.web.controller;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriTemplate;

import com.rajendarreddyj.spring.web.metric.IMetricService;
import com.rajendarreddyj.spring.web.util.LinkUtil;

@Controller
@RequestMapping(value = "/auth/")
public class RootController {

    @Autowired
    private IMetricService metricService;

    public RootController() {
        super();
    }

    // API

    // discover

    @RequestMapping(value = "admin", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void adminRoot(final HttpServletRequest request, final HttpServletResponse response) {
        final String rootUri = request.getRequestURL().toString();

        final URI fooUri = new UriTemplate("{rootUri}/{resource}").expand(rootUri, "foo");
        final String linkToFoo = LinkUtil.createLinkHeader(fooUri.toASCIIString(), "collection");
        response.addHeader("Link", linkToFoo);
    }

    @RequestMapping(value = "/metric", method = RequestMethod.GET)
    @ResponseBody
    public ConcurrentMap<String, ConcurrentHashMap<Integer, Integer>> getMetric() {
        return this.metricService.getFullMetric();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/status-metric", method = RequestMethod.GET)
    @ResponseBody
    public ConcurrentMap<Integer, Integer> getStatusMetric() {
        return this.metricService.getStatusMetric();
    }

    @RequestMapping(value = "/metric-graph", method = RequestMethod.GET)
    @ResponseBody
    public Object[][] drawMetric() {
        final Object[][] result = this.metricService.getGraphData();
        for (int i = 1; i < result[0].length; i++) {
            result[0][i] = result[0][i].toString();
        }
        return result;
    }

    @RequestMapping(value = "/admin/x", method = RequestMethod.GET)
    @ResponseBody
    public String sampleAdminPage() {
        return "Hello";
    }

    @RequestMapping(value = "/my-error-page", method = RequestMethod.GET)
    @ResponseBody
    public String sampleErrorPage() {
        return "Error Occurred";
    }

}
