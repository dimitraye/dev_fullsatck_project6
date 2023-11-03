import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';
import { MessageRequest } from '../../interfaces/api/messageRequest.interface';
import { MessageResponse } from '../../interfaces/api/messageResponse.interface';
import { CommentariesService } from '../../services/commentaries.service';
import { ArticlesService } from '../../services/articles.service';
import { ArticleView } from '../../interfaces/article.view.interface';
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
    private matSnackBar: MatSnackBar) {
    this.initCommentaryForm();
  }

  public ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id')!
    this.articlesService
      .detail(id)
      .subscribe((article: ArticleView) => this.article = article);
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
  }
  

  private initCommentaryForm() {
    this.commentaryForm = this.fb.group({
      content: ['', [Validators.required, Validators.min(10)]],
    });
  }

}
