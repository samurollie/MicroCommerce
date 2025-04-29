export type User = {
  id: number;
  username: string;
  password: string;
  email: string;
  name: string;
  phone?: string;
  isActive: boolean;
  roles: UserRoles[];
};

export enum UserRoles {
  USER = "USER",
  ADMIN = "ADMIN",
  SERVICE = "SERVICE",
}
