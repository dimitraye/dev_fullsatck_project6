import { Comment } from "./comment.interface";

export interface ArticleCreate {
    title: string;
    theme_id: number;
    content: string;
}
