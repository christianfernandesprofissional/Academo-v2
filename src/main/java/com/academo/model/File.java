package com.academo.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "files")
public class File implements Serializable {

    @Id
    @UuidGenerator
    @Column(name = "uuid", length = 255, nullable = false)
    private String uuid;

    @Column(name = "file_name", length = 255 ,nullable = false)
    private String fileName;  // Nome original do arquivo

    @Column(name = "path", columnDefinition = "TEXT", nullable = false)
    private String path;  // Caminho do arquivo depois de salvo

    @Column(name = "file_type", length = 255, nullable = false)
    private String fileType; // Tipo de arquivo

    @Column(name = "size")
    private Long size;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public File() {
    }

    public File(String fileName, String path, String fileType, Long size) {
        this.fileName = fileName;
        this.path = path;
        this.fileType = fileType;
        this.size = size;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Subject getSubject() { return subject; }

    public void setSubject(Subject subject) { this.subject = subject; }
}
