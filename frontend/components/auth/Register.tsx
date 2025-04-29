import { Logo } from "@/components/misc/icons";
import { Divider } from "@heroui/divider";
import { Card, CardHeader, CardBody, CardFooter } from "@heroui/card";
import { Input } from "@heroui/input";
import { Button } from "@heroui/button";
import { useState } from "react";
import { motion } from "framer-motion";
import { EyeClosedIcon, EyeIcon } from "lucide-react";

const Register = () => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [passwordConfirm, setPasswordConfirm] = useState("");
  const [errorUsername, setErrorUsername] = useState("");
  const [errorEmail, setErrorEmail] = useState("");
  const [errorPassword, setErrorPassword] = useState("");
  const [errorPasswordConfirm, setErrorPasswordConfirm] = useState("");
  const [status, setStatus] = useState(0);
  const [isVisible, setIsVisible] = useState(false);
  const toggleVisibility = () => setIsVisible(!isVisible);
  const [isVisibleConfirm, setIsVisibleConfirm] = useState(false);
  const toggleVisibilityConfirm = () => setIsVisibleConfirm(!isVisibleConfirm);

  const handleRegister = () => {
    setErrorEmail("");
    setErrorPassword("");
    if (username == "") {
      setErrorUsername("Debe ingresar un nombre de usuario");
    }
    if (email == "" || !validateEmail(email)) {
      setErrorEmail("Debe ingresar un email válido");
    }
    if (password != passwordConfirm && password != "" && passwordConfirm != "") {
      setErrorPassword("Las contraseñas no coinciden");
      setErrorPasswordConfirm("Las contraseñas no coinciden");
    }
    if (password == "") {
      setErrorPassword("Debe ingresar una contraseña");
    }
    if (passwordConfirm == "") {
      setErrorPasswordConfirm("Debe confirmar su contraseña");
    }
    if (username != "" && email != "" && password != "" && passwordConfirm != "" && validateEmail(email) && password == passwordConfirm) {
      sendPostRequest(username, email, password);
    }
  };

  const sendPostRequest = async (username: string, email: string, password: string) => {
    try {
      const response = await fetch(process.env.NEXT_PUBLIC_API_URL + "/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify({
          name: username,
          email: email,
          password: password,
        }),
      });
      const data = await response.json();
      if (response.ok) {
        console.log(data);
        setStatus(1);
      } else {
        console.error(data);
        setStatus(2);
      }
    } catch (error) {
      setStatus(2);
      console.error("Error:", error);
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
      <CardHeader className="flex gap-3 flex-row items-center justify-center w-full">
        <Logo />
        <div className="flex flex-col">
          <p className="text-lg font-bold">DFC</p>
          <p className="text-small text-default-500">Registrate para comenzar a adminstrar tiendas</p>
        </div>
      </CardHeader>
      <Divider />
      {!status && (
      <div>
      <CardBody className="flex items-center">
      <Input
          type="text"
          label="Usuario"
          className="max-w-[300px]"
          variant="bordered"
          color="secondary"
          isInvalid={errorUsername !== ""}
          isClearable
          errorMessage={errorUsername}
          onChange={(e) => {
            setUsername(e.target.value);
            setErrorUsername("");
          }}
        />
        <Input
          type="email"
          label="Email"
          className="max-w-[300px] mt-4 mb-4"
          variant="bordered"
          color="secondary"
          isInvalid={errorEmail !== ""}
          isClearable
          errorMessage={errorEmail}
          onChange={(e) => {
            setEmail(e.target.value);
            setErrorEmail("");
          }}
        />
        <Divider className="w-[50%]"/>
        <Input
          type={isVisible ? "text" : "password"}
          label="Contraseña"
          variant="bordered"
          color="secondary"
          className="max-w-[300px] mt-4"
          isInvalid={errorPassword !== ""}
          errorMessage={errorPassword}
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
          onChange={(e) => {
            setPassword(e.target.value);
            setErrorPassword("");
          }}
        />
        <Input
          type={isVisibleConfirm ? "text" : "password"}
          label="Confirmar Contraseña"
          variant="bordered"
          color="secondary"
          className="max-w-[300px] mt-4"
          isInvalid={errorPasswordConfirm !== ""}
          errorMessage={errorPasswordConfirm}
          endContent={
            <button
              aria-label="toggle password visibility"
              className="focus:outline-none"
              type="button"
              onClick={toggleVisibilityConfirm}
            >
              {isVisibleConfirm ? (
                <EyeClosedIcon className="text-2xl text-secondary pointer-events-none" />
              ) : (
                <EyeIcon className="text-2xl text-secondary pointer-events-none" />
              )}
            </button>
          }
          onChange={(e) => {
            setPasswordConfirm(e.target.value);
            setErrorPasswordConfirm("");
          }}
        />
      </CardBody>
      <Divider />
      <CardFooter className="flex flex-row justify-center">
        <Button className="min-w-[200px]" color="secondary" onPress={handleRegister}>
          Registrarse
        </Button>
      </CardFooter>
      </div>
      )}
      {status == 1 && (
        <CardBody  className="flex items-center">
          <div className="inline-block max-w-xl text-center justify-center">
            <span className="">Usuario registrado con&nbsp;</span>
            <span className="text-green-600">éxito.&nbsp;</span>
            <span className="">Ya puede iniciar sesión.&nbsp;</span>
          </div>
        </CardBody>
      )}
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

export default Register;