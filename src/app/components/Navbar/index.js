import Image from "next/image";
import icon from "@/public/icon.png";

const Navbar = () => {
  return (
    <div className="fixed top-0 left-0 w-full rounded-b-xl bg-sky-500 p-2 z-50">
      <div className="flex justify-center items-center">
        <Image
          src={icon}
          width={100}
          height={50}
          alt="Icon"
          className="h-auto"
        />
      </div>
    </div>
  );
};

export default Navbar;
