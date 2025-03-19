import { Logo } from "@/components/misc/icons";
import { Divider } from "@heroui/divider";
import { Card, CardHeader, CardBody, CardFooter } from "@heroui/card";
import { Input } from "@heroui/input";
import { Button } from "@heroui/button";
import { EyeClosedIcon, EyeIcon } from "lucide-react";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { motion } from "framer-motion";

const Login = () => {
  const router = useRouter();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorEmail, setErrorEmail] = useState("");
  const [errorPassword, setErrorPassword] = useState("");
  const [status, setStatus] = useState(0);

  const [isVisible, setIsVisible] = useState(false);
  const toggleVisibility = () => setIsVisible(!isVisible);

  const handleLogin = () => {
    setErrorEmail("");
    setErrorPassword("");
    if (email == "" || !validateEmail(email)) {
      setErrorEmail("Debe ingresar un email válido");
    }
    if (password == "") {
      setErrorPassword("Debe ingresar una contraseña");
    }
    if (email != "" && password != "" && validateEmail(email)) {
      sendPostRequest(email, password);
    }
  };

  const sendPostRequest = async (email: string, password: string) => {
    try {
      const response = await fetch("http://localhost:8080/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify({
          username: email,
          password: password,
        }),
      });
      const data = await response.json();
      if (response.ok) {
        console.log(data);
        localStorage.setItem("authToken", data.token);
        router.push("/panel");
      } else {
        console.error(data);
        setErrorPassword("Email o contraseña incorrectos");
        setErrorEmail("Email o contraseña incorrectos");
      }
    } catch (error) {
      console.error("Error:", error);
      setStatus(2);
    }
  };

  function validateEmail(email : string) {
    var regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
  }

  return (
    <motion.div layout style={{ height: "fit-content" }}>
    <Card
      isBlurred
      className="card border-none bg-background/60 dark:bg-default-100/50 w-[400px]"
      shadow="sm"
    >
      <CardHeader className="flex gap-3 flex-row-reverse items-center justify-center w-full">
        <Logo />
        <div className="flex flex-col">
          <p className="text-lg text-right font-bold">DFC</p>
          <p className="text-small text-default-500">Inicia sesión para adminstrar tus tiendas</p>
        </div>
      </CardHeader>
      <Divider />
      {!status && (
      <div>
      <CardBody className="flex items-center">
        <Input
          isClearable
          type="email"
          label="Email"
          className="max-w-[300px]"
          variant="bordered"
          color="secondary"
          isInvalid={errorEmail !== "" }
          errorMessage={errorEmail}
          onChange={(e) => {
            setEmail(e.target.value);
            setErrorEmail("");
          }}
        />
        <Input
          type={isVisible ? "text" : "password"}
          label="Contraseña"
          variant="bordered"
          color="secondary"
          className="max-w-[300px] mt-4"
          isInvalid={errorPassword !== ""}
          endContent={
            <button
              aria-label="toggle password visibility"
              className="focus:outline-none"
              type="button"
              onClick={toggleVisibility}
            >
              {isVisible ? (
                <EyeClosedIcon className="text-2xl text-secondary pointer-events-none" />
              ) : (
                <EyeIcon className="text-2xl text-secondary pointer-events-none" />
              )}
            </button>
          }
          errorMessage={errorPassword}
          onChange={(e) => {
            setPassword(e.target.value);
            setErrorPassword("");
          }}
        />
      </CardBody>
      <Divider />
      <CardFooter className="flex flex-row justify-center">
        <Button className="min-w-[200px]" color="secondary" onPress={handleLogin}>
          Iniciar Sesión
        </Button>
      </CardFooter>
      </div>)}
      {status == 2 && (
        <CardBody  className="flex items-center">
          <div className="inline-block max-w-xl text-center justify-center">
            <span className="">Ha ocurrido un&nbsp;</span>
            <span className="text-red-600">error.&nbsp;</span>
            <span className="">Intente de nuevo más tarde.&nbsp;</span>
          </div>
        </CardBody>
      )}
    </Card>
    </motion.div>
  );
}

export default Login;