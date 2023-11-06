import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';
import { MessageRequest } from '../../interfaces/api/messageRequest.interface';
import { MessageResponse } from '../../interfaces/api/messageResponse.interface';
import { CommentariesService } from '../../services/commentaries.service';
import { ArticlesService } from '../../services/articles.service';
import { ArticleView } from '../../interfaces/article.view.interface';

import { Comment } from "../../interfaces/comment.interface";

@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss']
})
export class DetailComponent implements OnInit {

  public commentaryForm!: FormGroup;
  public article: ArticleView | undefined;

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private commentariesService: CommentariesService,
    private articlesService: ArticlesService,
    private sessionService: SessionService,
    private matSnackBar: MatSnackBar,
    private cd: ChangeDetectorRef // Injectez ChangeDetectorRef
    ) {
    this.initCommentaryForm();
  }

  public ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id')!
    this.articlesService
      .detail(id)
      .subscribe((article: ArticleView) => {
        this.article = article;
        const commentariesSorted = this.sortByDate(article.commentaries, false);
        console.log("sorting comentaires", commentariesSorted);
        this.article.commentaries = commentariesSorted;
      });
  }

  public back() {
    window.history.back();
  }

  public sendCommentary(): void {
    const commentary = {
      article_id: this.article!.id,
      user_id: this.sessionService.user?.id,
      content: this.commentaryForm.value.content
    } as MessageRequest;
    console.log("commentary : ", commentary)

    this.commentariesService.send(commentary).subscribe(
      (messageResponse: MessageResponse) => {
        this.initCommentaryForm();
        this.matSnackBar.open(messageResponse.message, "Close", { duration: 3000 });
      });


      const userName = this.sessionService.user?.userName;

  if (userName) { // Vérifiez si userName est défini
    this.commentariesService.send(commentary).subscribe(
      (messageResponse: MessageResponse) => {
        


        const createdAtString = new Date().toLocaleString();
        // Ajoutez le nouveau commentaire à la liste
        this.article!.commentaries.unshift({
          userName: userName,
          content: this.commentaryForm.value.content,
          createdAt: createdAtString 
        });

        // Forcez la détection des changements pour mettre à jour la vue
        this.cd.detectChanges();
 
        this.matSnackBar.open(messageResponse.message, "Close", { duration: 3000 });
        this.initCommentaryForm();
      }
    );
  }
  }
  

  private initCommentaryForm() {
    this.commentaryForm = this.fb.group({
      content: ['', [Validators.required, Validators.min(10)]],
    });
  }

  sortByDate(comments: Comment[], ascending: boolean = true): Comment[] {
    // Utilisez la méthode `sort` pour trier le tableau
    return comments.sort((a, b) => {
      const dateA = new Date(a.createdAt).getTime();
      const dateB = new Date(b.createdAt).getTime();
  
      if (ascending) {
        // Tri croissant (du plus ancien au plus récent)
        return dateA - dateB;
      } else {
        // Tri décroissant (du plus récent au plus ancien)
        return dateB - dateA;
      }
    });
  }

}
