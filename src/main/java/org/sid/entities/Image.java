package org.sid.entities;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ConfigurationProperties(prefix = "file")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp()
    private Date date;
    @Transient
    private String location;
    @OneToOne()
    @JsonIncludeProperties(value = {"noms", "prenoms"})
    private Personnel personnel;

    public Image(String fileName, String fileDownloadUri, String fileType, long size, Personnel personnel) {
        super();
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
        this.personnel = personnel;
    }

}
