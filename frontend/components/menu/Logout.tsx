import { title, subtitle } from "@/components/misc/primitives";
import { Button } from "@heroui/button";

const Logout = () => {
  const handleLogout = async () => {
    try {
      await fetch("http://localhost:8080/logout", {
        method: "POST",
        credentials: "include",
      });
      localStorage.removeItem("authToken");
      window.location.href = "/";
    } catch (error) {
      console.error("Error durante el logout:", error);
    }
  };

  return (
    <div className="flex flex-col select-none w-[50%] gap-4 justify-start items-center">
      <h1 className={title()}>Cerrar Sesión</h1>
      <div className="flex flex-row justify-between w-full">
        <p className={subtitle()}>¿Estás seguro que quieres cerrar sesión?</p>
        <Button color="danger" onPress={handleLogout}>Confirmar</Button>
      </div>
    </div>
  );
}

export default Logout; 
