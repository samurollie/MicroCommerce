import { NavOptionProps } from "@/components/ui/navoption";
import { UserRoles } from "@/models/user";
import { UserService } from "@/services/user";

export function getAvailableRoutes(): NavOptionProps[] {
  const { user } = UserService();
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

  // Rotas não protegidas são sempre adicionadas
  routes.push(...unprotectedRoutes);

  // Se não houver usuário, retorna apenas rotas não protegidas
  if (!user) {
    return routes;
  }

  // Adiciona rotas protegidas para usuários logados
  routes.push(...protectedRoutes);

  // Verifica se o usuário tem role de admin
  if (
    user.roles &&
    Array.isArray(user.roles) &&
    user.roles.includes(UserRoles.ADMIN)
  ) {
    routes.push(...AdminRoutes);
  }

  return routes;
}
