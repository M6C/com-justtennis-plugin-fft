package com.justtennis.plugin.fb.model;

import org.cameleon.android.shared.model.ModelObject;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Publication resource
 * used by 'Publication' feature
 */
public class Publication implements ModelObject {

    public final String publicationId;
    public final String idRH;
    public final String channel;
    public final String firstName;
    public final String lastName;
    public final Date postDate;
    public final String message;

    public Publication(String publicationId, String idRH, String channel, String firstName, String lastName,
                 Date postDate, String message) {

        this.publicationId = publicationId;
        this.idRH = idRH;
        this.channel = channel;
        this.firstName = firstName;
        this.lastName = lastName;
        this.postDate = postDate;
        this.message = message;
    }

    public static void sortByPostDateDesc(List<Publication> publications) {
        Collections.sort(publications, (publication1, publication2) -> publication2.postDate.compareTo(publication1.postDate));
    }
}
