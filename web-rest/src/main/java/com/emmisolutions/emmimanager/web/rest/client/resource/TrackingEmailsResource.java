package com.emmisolutions.emmimanager.web.rest.client.resource;

import com.emmisolutions.emmimanager.service.mail.TrackingService;
import org.h2.util.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;

import static com.emmisolutions.emmimanager.service.mail.TrackingService.SIGNATURE_VARIABLE_NAME;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.IMAGE_GIF_VALUE;

/**
 * Endpoints for email tracking
 */
@RestController
@RequestMapping(value = "/webapi-client")
public class TrackingEmailsResource {

    @Resource
    TrackingService trackingService;

    private byte[] blankImage;

    /**
     * Creates a URITemplate based email read link that should be used as a GET.
     * E.g. https://server:port/webapi-client/read/SIGNATURE_VARIABLE_NAME
     *
     * @return the uri template as a string
     */
    public static String emailViewedTrackingLink() {
        return UriComponentsBuilder.fromHttpUrl(linkTo(methodOn(
                                TrackingEmailsResource.class).track(SIGNATURE_VARIABLE_NAME)
                ).withSelfRel().getHref()
        ).build(true).toUriString();
    }

    @PostConstruct
    private void init() throws IOException {
        blankImage = IOUtils.readBytesAndClose(new ClassPathResource("images/tracking.gif").getInputStream(), -1);
    }

    /**
     * GET to record an email view
     *
     * @param signature used to update the EmailTemplateTracking object
     * @return 200 (OK): wrapping a byte[]
     */
    @RequestMapping(value = "/email/view/{signature}.gif", method = RequestMethod.GET,
            produces = IMAGE_GIF_VALUE)
    public ResponseEntity<byte[]> track(@PathVariable("signature") String signature) {
        trackingService.viewed(signature);
        return ResponseEntity.ok(blankImage);
    }
}
