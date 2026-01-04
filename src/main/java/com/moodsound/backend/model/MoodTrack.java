package com.moodsound.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mood_tracks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"mood_id", "track_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoodTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mood_id", nullable = false)
    private Mood mood;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;

    @Column(name = "order_position")
    private Integer orderPosition;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public Integer getOrderPosition() {
        return orderPosition;
    }

    public void setOrderPosition(Integer orderPosition) {
        this.orderPosition = orderPosition;
    }
}