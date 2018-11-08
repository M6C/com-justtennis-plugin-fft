package com.justtennis.plugin.fb.dto;

import com.justtennis.plugin.fb.enums.STATUS_PUBLICATION;

import java.io.Serializable;
import java.util.Date;

/**
 * Publication resource
 * used by 'Publication' feature
 */
public class PublicationDto implements Serializable {

    public final Long id;
    public final Date postDate;
    public final String message;
    public Date publishDate;
    public STATUS_PUBLICATION statusPublication = STATUS_PUBLICATION.CREATED;

    public PublicationDto(Long id, Date postDate, String message) {
        this.id = id;
        this.postDate = postDate;
        this.message = message;
    }

    @Override
    public String toString() {
        return "PublicationDto{" +
                "id=" + id +
                ", postDate=" + postDate +
                ", message='" + message + '\'' +
                ", publishDate=" + publishDate +
                ", statusPublication=" + statusPublication +
                '}';
    }
}
