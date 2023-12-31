import { Theme } from "../features/themes/interfaces/theme.interface";

export interface User {
	id: number,
	userName: string,
	email: string,
	themes : Theme[]
}