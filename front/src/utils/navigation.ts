import { NavOptionProps } from "@/components/ui/navoption";
import { UserRoles } from "@/models/user";
import { UserService } from "@/services/user";

export function getAvailableRoutes(): NavOptionProps[] {
  const { getCurrentUser } = UserService();
  const routes: NavOptionProps[] = [];

  const unprotectedRoutes: NavOptionProps[] = [
    //Rotas que todos tem acesso
    {
      label: "Catálogo",
      href: "/catalogue",
    },
  ];
  const protectedRoutes: NavOptionProps[] = [
    //Rotas que só usuários logados tem acesso
    { label: "Carrinho", href: "/cart" },
    { label: "Minhas Compras", href: "/orders" },
  ];
  const AdminRoutes: NavOptionProps[] = [
    //Rotas que só usuários com permissão de admin tem acesso
    { label: "Configurações", href: "/setup" },
  ];

  const currentUser = getCurrentUser();

  routes.push(...unprotectedRoutes);

  if (currentUser) {
    routes.push(...protectedRoutes);
    if (currentUser.roles.includes(UserRoles.ADMIN)) {
      routes.push(...AdminRoutes);
    }
  }

  return routes;
}
