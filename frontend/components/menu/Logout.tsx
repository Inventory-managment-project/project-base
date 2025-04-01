import { title, subtitle } from "@/components/misc/primitives";
import { Button } from "@heroui/button";

const Logout = () => {
  const handleLogout = async () => {
    try {
      const res = await fetch("http://localhost:8080/logout", {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${localStorage.getItem("authToken")}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ token: localStorage.getItem("authToken") }),
      });
      localStorage.clear();
      localStorage.setItem("theme", "light");
      document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
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
