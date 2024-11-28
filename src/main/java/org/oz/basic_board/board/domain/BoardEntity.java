package org.oz.basic_board.board.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.oz.basic_board.common.domain.AttachFile;
import org.oz.basic_board.common.domain.BasicEntity;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_board")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardEntity extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;
    private String content;
    private String writer;
    private Integer btype;

    @ElementCollection
    @Builder.Default
    @CollectionTable(
            name = "tbl_board_files",
            joinColumns = @JoinColumn(name = "bno"))
    private Set<AttachFile> attachFiles = new HashSet<>();

    public void changeTitle(String title) {this.title = title;}
    public void changeContent(String content) {this.content = content;}
    public void addFile(String fileName) {
        if (fileName == null){
            attachFiles = new HashSet<>();
        }
        attachFiles.add(new AttachFile(attachFiles.size(), fileName));
    }
    public void removeFiles() {
        attachFiles.clear();
    }
}
