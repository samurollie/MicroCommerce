import { UserRoles } from "../user";

export type RegisterDTO = {
  username: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  roles: Set<UserRoles>;
};
