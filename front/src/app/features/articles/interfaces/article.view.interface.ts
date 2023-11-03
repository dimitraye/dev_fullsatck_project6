import { Comment } from "./comment.interface";

export interface ArticleView {
    id: number;
    title: string;
    content: string;
    theme: string;
    createdAt: string;
    userName: string;
    commentaries: Comment[];
}
