import { NavOptionProps } from "@/components/ui/navoption";
import { UserRoles } from "@/models/user";
import { userService } from "@/services/user";

export function getAvailableRoutes(): NavOptionProps[] {
  const { getCurrentUser } = userService();

  const routes: NavOptionProps[] = [
    {
      label: "Catálogo",
      href: "/catalogue",
    },
    { label: "Carrinho", href: "/cart" },
    { label: "Minhas Compras", href: "/orders" },
  ];
  const AdminRoutes: NavOptionProps[] = [{ label: "Configurações", href: "/setup" }];

  const currentUser = getCurrentUser();

  if (currentUser.roles.includes(UserRoles.ADMIN)) {
    routes.push(...AdminRoutes);
  }

  return routes;
}
