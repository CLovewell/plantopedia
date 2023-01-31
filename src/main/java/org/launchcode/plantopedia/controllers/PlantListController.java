package org.launchcode.plantopedia.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.launchcode.plantopedia.responses.ClientTokenResponse;
import org.launchcode.plantopedia.responses.links.ListLinks;
import org.launchcode.plantopedia.responses.lists.PlantListResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class PlantListController {

    @SuppressWarnings("SpellCheckingInspection")
    @Value("${TREFLE_API_TOKEN}")
    private String apiKey;
    public static final String API_SCHEME = "https";
    public static final String API_HOST = "trefle.io";
    public static final String API_PATH ="/api/v1/";
    public static final String BASE_API_URI = API_SCHEME + "://" + API_HOST + API_PATH;
    public static final String CLIENT_TOKEN_REQUEST_PATH = "https://trefle.io/api/auth/claim";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String listAllPlants(Model model, HttpServletRequest request) {

        String token = getClientToken(request.getRequestURI()).getToken();
        URI listPlantsUri = URI.create(BASE_API_URI + "plants?token=" + token);
        RestTemplate restTemplate = new RestTemplate();

        PlantListResponse response = restTemplate.getForObject(
                listPlantsUri, PlantListResponse.class);
        if (response != null) {
            model.addAttribute("plants", response.getData());
            ListLinks links = response.getLinks();
            try {
                URI trefle = new URI(API_SCHEME, API_HOST, null, null);

                URI first = trefle.resolve(links.getFirst());
                model.addAttribute("firstLinkScheme", first.getScheme());
                model.addAttribute("firstLinkAuthority", first.getAuthority());
                model.addAttribute("firstLinkPath", first.getPath());
                model.addAttribute("firstLinkQuery", first.getQuery());

                URI last = trefle.resolve(links.getLast());
                model.addAttribute("lastLinkScheme", last.getScheme());
                model.addAttribute("lastLinkAuthority", last.getAuthority());
                model.addAttribute("lastLinkPath", last.getPath());
                model.addAttribute("lastLinkQuery", last.getQuery());

                if (links.getPrev() != null) {
                    URI prev = trefle.resolve(links.getPrev());
                    model.addAttribute("prevLinkScheme", prev.getScheme());
                    model.addAttribute("prevLinkAuthority", prev.getAuthority());
                    model.addAttribute("prevLinkPath", prev.getPath());
                    model.addAttribute("prevLinkQuery", prev.getQuery());
                }

                if (links.getNext() != null) {
                    URI next = trefle.resolve(links.getNext());
                    model.addAttribute("nextLinkScheme", next.getScheme());
                    model.addAttribute("nextLinkAuthority", next.getAuthority());
                    model.addAttribute("nextLinkPath", next.getPath());
                    model.addAttribute("nextLinkQuery", next.getQuery());
                }
            }
            catch (URISyntaxException e) {
                model.addAttribute("error", "Bad URI");
                return "listPlants";
            }
            model.addAttribute("links", links);
            model.addAttribute("meta", response.getMeta());
        }

        return "listPlants";
    }

    @RequestMapping(value = "/plants", method = RequestMethod.GET)
    public String plantListByPage(Model model, HttpServletRequest request,
                                  @RequestParam(value = "page", defaultValue = "0") String page) {

        if (page.equals("0")) {
            return "redirect:/";
        }

        String token = getClientToken(request.getRequestURI()).getToken();
        URI listPlantsUri = URI.create(BASE_API_URI + "plants?token=" + token + "&page=" + page);
        RestTemplate restTemplate = new RestTemplate();

        PlantListResponse response = restTemplate.getForObject(
                listPlantsUri, PlantListResponse.class);

        if (response != null) {
            model.addAttribute("plants", response.getData());
            ListLinks links = response.getLinks();
            try {
                URI trefle = new URI(API_SCHEME, API_HOST, null, null);

                URI first = trefle.resolve(links.getFirst());
                model.addAttribute("firstLinkScheme", first.getScheme());
                model.addAttribute("firstLinkAuthority", first.getAuthority());
                model.addAttribute("firstLinkPath", first.getPath());
                model.addAttribute("firstLinkQuery", first.getQuery());

                URI last = trefle.resolve(links.getLast());
                model.addAttribute("lastLinkScheme", last.getScheme());
                model.addAttribute("lastLinkAuthority", last.getAuthority());
                model.addAttribute("lastLinkPath", last.getPath());
                model.addAttribute("lastLinkQuery", last.getQuery());

                if (links.getPrev() != null) {
                    URI prev = trefle.resolve(links.getPrev());
                    model.addAttribute("prevLinkScheme", prev.getScheme());
                    model.addAttribute("prevLinkAuthority", prev.getAuthority());
                    model.addAttribute("prevLinkPath", prev.getPath());
                    model.addAttribute("prevLinkQuery", prev.getQuery());
                }

                if (links.getNext() != null) {
                    URI next = trefle.resolve(links.getNext());
                    model.addAttribute("nextLinkScheme", next.getScheme());
                    model.addAttribute("nextLinkAuthority", next.getAuthority());
                    model.addAttribute("nextLinkPath", next.getPath());
                    model.addAttribute("nextLinkQuery", next.getQuery());
                }
            }
            catch (URISyntaxException e) {
                model.addAttribute("error", "Bad URI");
                return "listPlants";
            }
            model.addAttribute("links", links);
            model.addAttribute("meta", response.getMeta());
        }

        return "listPlants";
    }

    @RequestMapping(value = "/plants/detail/{plantId}", method = RequestMethod.GET)
    public String plantDetails(@PathVariable int plantId, HttpServletRequest request) {

        String token = getClientToken(request.getRequestURI()).getToken();
        return "redirect:https://trefle.io/api/v1/plants/" + plantId + "?token=" + token;
    }

    @RequestMapping(value = "/parse-pagination-link", method = RequestMethod.GET)
    public String parsePaginationLink(Model model, @RequestParam String scheme,
                                      @RequestParam String authority,
                                      @RequestParam String path,
                                      @RequestParam String query, HttpServletRequest request) {
        RestTemplate template = new RestTemplate();
        String token = getClientToken(request.getRequestURI()).getToken();
        URI uri = new URI(scheme, authority, path, query, null);
        if (q.equals("")) {
            uri = API_SCHEME + "://" + API_HOST + link + "&token=" + token;
        }
        else {
            uri = API_SCHEME + "://" + API_HOST + link + "&q=" + q + "&token=" + token;
        }
        PlantListResponse response = template.getForObject(
                uri,
                PlantListResponse.class);
        if (response != null) {
            model.addAttribute("plants", response.getData());
            model.addAttribute("links", response.getLinks());
            model.addAttribute("meta", response.getMeta());
        }
        return "listPlants";
    }

    @RequestMapping(value = "/plants/search")
    public String searchPlants(Model model, @RequestParam String q, HttpServletRequest request) {

        RestTemplate restTemplate = new RestTemplate();
        String token = getClientToken(request.getRequestURI()).getToken();

        if (q.equals("")) {
            model.addAttribute("error", "Search term cannot be blank");
            String listPlantsUri = BASE_API_URI + "plants?token=" + token;

            PlantListResponse plantListResponse = restTemplate.getForObject(
                    listPlantsUri, PlantListResponse.class);
            if (plantListResponse != null) {
                model.addAttribute("plants", plantListResponse.getData());
                model.addAttribute("links", plantListResponse.getLinks());
                model.addAttribute("meta", plantListResponse.getMeta());
            }
        }
        else {
            PlantListResponse response = restTemplate.getForObject(
                    BASE_API_URI + "plants/search?q=" + q + "&token=" + token,
                    PlantListResponse.class);
            if (response != null) {
                model.addAttribute("plants", response.getData());
                model.addAttribute("links", response.getLinks());
                model.addAttribute("meta", response.getMeta());
            }
            model.addAttribute("q", q);
        }
        return "listPlants";
    }

    public ClientTokenResponse getClientToken(String origin) {
        ClientTokenRequest request = new ClientTokenRequest(origin);
        HttpEntity<ClientTokenRequest> entity = new HttpEntity<>(request);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(URI.create(CLIENT_TOKEN_REQUEST_PATH + "?token=" + apiKey),
                entity, ClientTokenResponse.class);
    }

    public String getPage(String uri) {
        Pattern p = Pattern.compile("page=\\d+");
        Matcher m = p.matcher(uri);
        boolean b = m.find();
        if (!b) {
            return "0";
        }
        else {
            return m.group().replace("page=", "");
        }
    }

    public static class ClientTokenRequest {
        private String origin;
        private String ip;

        public ClientTokenRequest(String origin) {
            this.origin = origin;
            this.ip = null;
        }

        public ClientTokenRequest(String origin, String ip) {
            this.origin = origin;
            this.ip = ip;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }
    }
}
